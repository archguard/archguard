package org.archguard.scanner.analyser.backend.go

import chapi.ast.goast.GoAnalyser
import chapi.domain.core.CodeContainer
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GoProtobufConsumerAnalyserTest {
    @Test
    fun analyzeAndMapCodePathsHelloWorld() {
        /// if windows return
        if (System.getProperty("os.name").contains("Windows")) {
            return
        }

        @Language("Go") val clientCode = """
package client
            
import "go-common/library/net/rpc"
   
const _userTotalLike = "RPC.UserTotalLike"

type Service struct {
	client *rpc.Client2
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

type Dao struct {
	dao    *dao.Dao
	close  bool
    thumbup  *thumbup.Service
}
"""

        @Language("go") val thirdParty = """
package service

import (
    thumbup "go-common/app/service/main/thumbup/rpc/client"
)

func (s *Dao) likeVideos(c context.Context, mid int64, pcy bool) (list []*model.DyActItem, err error) {
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
""".trimIndent()

        val client = GoAnalyser().analysis(clientCode, "/root/go-common/app/service/main/thumbup/rpc/client/thumpup.go")
            .apply { fillImports() }
        val server = GoAnalyser().analysis(serverCode, "/root/go-common/app/service/main/thumbup/server/grpc/server.go")
            .apply { fillImports() }
        // /Users/phodal/test/go-common/app/interface/main/space/service/service.go
        val service = GoAnalyser().analysis(serviceCode, "/root/go-common/app/interface/main/space/service/service.go")
            .apply { fillImports() }
        val third = GoAnalyser().analysis(thirdParty, "/root/go-common/app/interface/main/space/service/dynamic.go")
            .apply { fillImports() }

        val containers = listOf(client, server, service, third)
        val dataStructs = containers.map { it.DataStructures }.flatten()

        val consumerAnalyser = GoProtobufConsumerAnalyser(dataStructs, "/root/go-common")
        val sourceTargetMap = consumerAnalyser.analyzeAndMapCodePaths(third.DataStructures)
        assert(sourceTargetMap.isNotEmpty())
        assertEquals(
            listOf("go-common/app/interface/main/space/service/dynamic\$Dao.likeVideos"),
            sourceTargetMap["go-common/app/service/main/thumbup/rpc/client/thumpup\$Service.UserTotalLike"]
        )

        val clientMap = consumerAnalyser.analyzeAndMapCodePaths(client.DataStructures)
        assert(clientMap.isNotEmpty())
        assertEquals(
            listOf("go-common/app/service/main/thumbup/rpc/client/thumpup\$Service.UserTotalLike"),
            clientMap["RPC.UserTotalLike"]
        )

        val demands = consumerAnalyser.analysis()
        assertEquals(
            listOf(
                "RPC.UserTotalLike",
                "go-common/app/service/main/thumbup/rpc/client/thumpup\$Service.UserTotalLike",
                "go-common/app/interface/main/space/service/dynamic\$Dao.likeVideos"
            ), demands.first().call_routes
        )
    }

    private fun CodeContainer.fillImports() {
        DataStructures.forEach { ds ->
            ds.Imports = this.Imports
        }
    }
//
//    @Test
//    fun should_parse_for_projects() {
//        val file = File("/Volumes/source/archguard/archguard-backend/0_codes.json")
//        val dataStructs: List<CodeDataStruct> = Json.decodeFromString(file.readText())
//
//        val consumerAnalyser = GoProtobufConsumerAnalyser(dataStructs, "/Users/phodal/test/go-common")
//        val sourceTargetMap = consumerAnalyser.analysis()
//
//        sourceTargetMap.forEach {
//            if (it.call_routes.size > 2) {
//                println(it)
//            }
//        }
//    }
}