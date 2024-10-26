package main

import (
    "database/sql"
    "fmt"
    "log"
    _ "github.com/go-sql-driver/mysql" // 导入 MySQL 驱动
)

const (
	_getVipStatus           = "SELECT id,mid,vip_status FROM vip_user_info WHERE mid=?"
)

// 定义 User 结构体，映射数据库中的表
type User struct {
    ID   int
    Name string
    Age  int
}

// 查询用户信息
func queryUsers(db *sql.DB, age int) ([]User, error) {
    query := "SELECT id, name, age FROM users WHERE age >= ?"
    rows, err := db.Query(query, age)
    if err != nil {
        return nil, err
    }
    defer rows.Close()

    var users []User
    for rows.Next() {
        var user User
        if err := rows.Scan(&user.ID, &user.Name, &user.Age); err != nil {
            return nil, err
        }
        users = append(users, user)
    }

    if err = rows.Err(); err != nil {
        return nil, err
    }

    return users, nil
}

// VipStatus get user vip status.
func (d *Dao) VipStatus(c context.Context, mid int64) (res *model.VipUserInfo, err error) {
	row := d.db.QueryRow(c, _getVipStatus, mid)
	res = new(model.VipUserInfo)
	if err = row.Scan(&res.ID, &res.Mid, &res.Status); err != nil {
		if err == sql.ErrNoRows {
			err = nil
			res = nil
		} else {
			err = errors.Wrapf(err, "d.VipStatus(%d)", mid)
		}
	}
	return
}
