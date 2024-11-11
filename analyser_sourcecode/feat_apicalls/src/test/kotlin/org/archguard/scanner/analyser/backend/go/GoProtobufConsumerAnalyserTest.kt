package org.archguard.scanner.analyser.backend.go

import chapi.ast.goast.GoAnalyser
import chapi.domain.core.CodeContainer
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GoProtobufConsumerAnalyserTest {
    @Test
    fun analyzeAndMapCodePathsHelloWorld() {
        @Language("Go") val clientCode = """
package client
            
import "net/rpc"
   
const _userTotalLike = "RPC.UserTotalLike"

type Service struct {
	client *rpc.Client
}

func (s *Service) UserTotalLike(c context.Context, arg *model.ArgUserLikes) (res *model.UserTotalLike, err error) {
	err = s.client.Call(c, _userTotalLike, arg, &res)
	return
}
"""

        @Language("go") val serverCode = """
package server

import "service"

type server struct {
	s *service.Service
}
   
func (r server) UserLikes(c context.Context, req *pb.UserLikesReq) (reply *pb.UserLikesReply, err error) {
	res, err := r.s.UserTotalLike(c, req.Business, req.Mid, int(req.Pn), int(req.Ps))
	reply = &pb.UserLikesReply{}
	if res != nil {
		reply.Total = int64(res.Total)
		for _, item := range res.List {
			reply.Items = append(reply.Items, &pb.ItemRecord{
				MessageID: item.MessageID,
				Time:      item.Time,
			})
		}
	}
	return
}
"""

        @Language("go") val serviceCode = """
package service

import (
   "go-common/app/service/main/thumbup/dao"
)

type Service struct {
	dao    *dao.Dao
	close  bool
}

func (s *Service) UserTotalLike(c context.Context, business string, mid int64, pn, ps int) (res *model.UserTotalLike, err error) {
	group, ctx := errgroup.WithContext(c)
	res = &model.UserTotalLike{}
	group.Go(func() (err error) {
		res.List, err = s.UserLikes(ctx, business, mid, pn, ps)
		return
	})
	group.Go(func() (err error) {
		res.Total, err = s.userTotal(ctx, business, mid)
		return
	})
	if err = group.Wait(); err != nil {
		res = nil
		return
	}
	return
}
"""

        @Language("go") val thirdParty = """
package service

import (
    thumbup "go-common/app/service/main/thumbup/rpc/client"
)

type Service struct {
   dao *dao.Dao
   thumbup  *thumbup.Service
}

func (s *Service) likeVideos(c context.Context, mid int64, pcy bool) (list []*model.DyActItem, err error) {
	var (
		likes *thumbup.UserTotalLike
		ip    = metadata.String(c, metadata.RemoteIP)
	)
	arg := &thumbup.ArgUserLikes{Mid: mid, Business: _businessLike, Pn: 1, Ps: _likeVideoCnt, RealIP: ip}
	if likes, err = s.thumbup.UserTotalLike(c, arg); err != nil {
		log.Error("s.thumbup.UserTotalLike(%d) error(%v)", mid, err)
		return
	}
	if likes != nil {
		for _, v := range likes.List {
			if v.MessageID > 0 {
				list = append(list, &model.DyActItem{Aid: v.MessageID, Type: _dyTypeLike, ActionTime: int64(v.Time), Privacy: pcy})
			}
		}
	}
	return
}

type B struct {

}

""".trimIndent()

        val client = GoAnalyser().analysis(clientCode, "go-common/app/service/main/thumbup/rpc/client.go")
            .apply { fillImports() }
        val server = GoAnalyser().analysis(serverCode, "go-common/app/service/main/thumbup/server/grpc/server.go")
            .apply { fillImports() }
        val service = GoAnalyser().analysis(serviceCode, "go-common/app/service/main/thumbup/service/service.go")
            .apply { fillImports() }
        val third = GoAnalyser().analysis(thirdParty, "go-common/app/interface/main/space/service/dynamic.go")
            .apply { fillImports() }

        val containers = listOf(client, server, service, third)
        val dataStructs = containers.map { it.DataStructures }.flatten()

        val consumerAnalyser = GoProtobufConsumerAnalyser(dataStructs, "")
        val sourceTargetMap = consumerAnalyser.analyzeAndMapCodePaths(third.DataStructures)
        assert(sourceTargetMap.isNotEmpty())
        assertEquals(
            listOf("go-common/app/interface/main/space/service/dynamic\$Service.likeVideos"),
            sourceTargetMap["go-common/app/service/main/thumbup/rpc/client\$Service.UserTotalLike"]
        )

        val clientMap = consumerAnalyser.analyzeAndMapCodePaths(client.DataStructures)
        assert(clientMap.isNotEmpty())
        assertEquals(
            listOf("go-common/app/service/main/thumbup/rpc/client\$Service.UserTotalLike"),
            clientMap["RPC.UserTotalLike"]
        )

        val buildCallChain =
            consumerAnalyser.buildCallChain((clientMap + sourceTargetMap).toMutableMap(), "RPC.UserTotalLike")
        assert(buildCallChain.isNotEmpty())

        // RPC.UserTotalLike, go-common/app/service/main/thumbup/rpc/client$Service.UserTotalLike, go-common/app/interface/main/space/service/dynamic$Service.likeVideos
        assertEquals(
            listOf(
                "RPC.UserTotalLike",
                "go-common/app/service/main/thumbup/rpc/client\$Service.UserTotalLike",
                "go-common/app/interface/main/space/service/dynamic\$Service.likeVideos"
            ),
            buildCallChain
        )
    }

    private fun CodeContainer.fillImports() {
        DataStructures.forEach { ds ->
            ds.Imports = this.Imports
        }
    }
}