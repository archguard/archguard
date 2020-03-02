## 删除config
+ ##### URL地址: `/dependency/package`
+ ##### 请求方式: `GET`

+ ##### response:
    ###### 样例
    ```Json
  {
      "nodes": [
        {
          "id": 1,
          "title": "application",
          "parent": 0
        },
        {
          "id": 2,
          "title": "fix",
          "parent": 1
        }
      ],
      "edges": [
        {
          "a": 1,
          "b": 2,
          "num": 10
        }
      ]
  }
    ```
  
    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :----:|:---:|:---:|:---:|:---:
    `nodes` | 节点 | `数组` | | N
    `edges` | 依赖关系 | `数组` || N
    `id` | 唯一标识 | `int` | 后台按顺序生成 | N
    `title` | 包名 | `String` |只包含最后一级，不包含于parent重合部分| N
    `parent` | 上一级包名 | `int` || N
    `a` | 调用节点 | `int` || N
    `b` | 被调用节点 | `int` || N
    `num` | 依赖数量 | `int` | 值方法的调用数量 | N

+ #### Status
    状态码 | 说明 | 备注
    :---:|:---:|:---:
    `200` | 正常返回 |
