package org.archguard.scanner.analyser.backend.go

import chapi.ast.goast.GoAnalyser
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeImport
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class GoApiConsumerAnalyserTest {
    @Test
    fun analysisHelloWorld() {
        @Language("Go") val clientCode = """
package client
            
import "net/rpc"
   
const _userTotalLike = "RPC.UserTotalLike"

type Service struct {
	client *rpc.Client
}

// UserLikes user likes list
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

// RawStats get stat changes
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
/// /Users/phodal/test/go-common/app/service/main/thumbup/rpc/client/thumbup.go
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
""".trimIndent()

        val client = GoAnalyser().analysis(clientCode, "client.go")
        val server = GoAnalyser().analysis(serverCode, "server.go")
        val service = GoAnalyser().analysis(serviceCode, "service.go")
        /// /Users/phodal/test/go-project/app/interface/main/space/service/dynamic.go
        val third = GoAnalyser().analysis(thirdParty, "go-project/app/interface/main/space/service/dynamic.go")

        val containers = listOf(client, server, service, third)

        val allType: Map<String, CodeDataStruct> =
            containers.map { it.DataStructures }.flatten().associateBy { it.NodeName }

        third.DataStructures.forEach { ds ->
            ds.Functions.forEach { function ->
                function.FunctionCalls.forEach { call ->
                    if (call.NodeName.startsWith("Service") && call.NodeName.contains(".")) {
                        val struct = call.NodeName.split(".").first()
                        val model = call.NodeName.split(".").last()

                        val codeDataStruct = allType[struct]
                        val field = codeDataStruct?.Fields?.filter {
                            it.TypeValue == model
                        }
                        val importMap: MutableMap<String, CodeImport> = mutableMapOf()
                        third.Imports.forEach { codeImport ->
                            codeImport.UsageName.forEach { it ->
                                importMap[it] = codeImport
                            }
                        }

                        field?.forEach { codeField ->
                            /// remove after "." for codeField.TypeType
                            val typeType = codeField.TypeType.removePrefix("*")
                            val importPath = typeType.split(".").first()
                            val other = typeType.removePrefix(importPath + ".")

                            importPath.let {
                                val import = importMap[it]
                                println(call.NodeName + "-->" + typeType + "-->" + call.FunctionName)
                                println(import)
                                println(codeField)
                            }
                        }
                    }
                }
            }
        }
    }
}