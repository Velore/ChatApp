CREATE DATABASE chatapp CHARACTER SET utf8;

CREATE TABLE IF NOT EXISTS chat_user(
    # 用户id
    uid VARCHAR(10) NOT NULL PRIMARY KEY comment '用户id',
    # 用户名字
    user_name VARCHAR(10) comment '用户名',
    # 用户密码
    pwd VARCHAR(16) NOT NULL comment '密码',

    register_time TIMESTAMP NOT NULL comment '注册时间',

    last_online_time TIMESTAMP NOT NULL comment '最后登录时间'
) comment '用户';

CREATE TABLE IF NOT EXISTS chat_group(
    # 群主id
    owner_id VARCHAR(10) NOT NULL comment '群主id',
    # 群组id
    gid VARCHAR(10) NOT NULL PRIMARY KEY comment '群组id',
    # 群组名称
    group_name VARCHAR(10) NOT NULL DEFAULT gid comment '群组名称',
    # 建群时间
    establish_time TIMESTAMP NOT NULL comment '建群时间',
    FOREIGN KEY (owner_id) REFERENCES chat_user(uid)
) comment '聊天群组';

CREATE TABLE IF NOT EXISTS group_member(
    # 成员id
    member_id VARCHAR(10) NOT NULL comment '成员id',
    # 群组id
    gid VARCHAR(10) NOT NULL comment '群组id',
    # 成员在群组的权限
    member_auth VARCHAR(6) NOT NULL DEFAULT 'member' comment '成员权限',
    # 加入群组的时间
    join_time TIMESTAMP NOT NULL comment '成员加入时间',

    FOREIGN KEY (member_id) REFERENCES chat_user(uid),
    FOREIGN KEY (gid) REFERENCES chat_group(gid)
) comment '群组成员';

CREATE TABLE IF NOT EXISTS chat_message(
    # 消息发送时间
    send_time TIMESTAMP NOT NULL comment '发送时间',
    # 消息发送者id
    sender_id VARCHAR(10) NOT NULL comment '发送者',
    # 消息发送的群组id
    gid VARCHAR(10) NOT NULL comment '消息所在群组',
    # 消息的内容
    msg_str VARCHAR(100) NOT NULL comment '消息内容',
    FOREIGN KEY (sender_id) REFERENCES chat_user(uid),
    FOREIGN KEY (gid) REFERENCES chat_group(gid)
) comment '聊天消息';

CREATE TABLE user_attention(
    #关注者id
    follower_id VARCHAR(10) NOT NULL comment '关注者',
    #被关注者id
    accept_id VARCHAR(10) NOT NULL comment '被关注者',
    #关注的时间
    follow_time TIMESTAMP NOT NULL comment '关注时间'
) comment '关注列表';

# 插入用户数据
INSERT INTO
    chat_user(uid, user_name, pwd, register_time, last_online_time) VALUES('u1', 'n1', 'p1', '2001-01-01 00:00:00', '2001-01-01 00:00:01');
INSERT INTO
    chat_user(uid, user_name, pwd, register_time, last_online_time) VALUES('u2', 'n2', 'p2', '2001-01-01 00:00:01', '2001-01-01 00:00:02');
INSERT INTO
    chat_user(uid, user_name, pwd, register_time, last_online_time) VALUES('u3', 'n3', 'p3', '2001-01-01 00:00:02', '2001-01-01 00:00:03');
INSERT INTO
    chat_user(uid, user_name, pwd, register_time, last_online_time) VALUES('u4', 'n4', 'p4', '2001-01-01 00:00:03', '2001-01-01 00:00:04');
INSERT INTO
    chat_user(uid, user_name, pwd, register_time, last_online_time) VALUES('u5', 'n5', 'p5', '2001-01-01 00:00:04', '2001-01-01 00:00:05');
INSERT INTO
    chat_user(uid, user_name, pwd, register_time, last_online_time) VALUES('u6', 'n6', 'p6', '2001-01-01 00:00:05', '2001-01-01 00:00:06');

# 插入群组数据
INSERT INTO
    chat_group(owner_id, gid, group_name, establish_time) VALUES('u1', '10001', 'name10001', '2001-01-01 00:00:05');
INSERT INTO
    chat_group(owner_id, gid, group_name, establish_time) VALUES('u4', '10002', 'name10002', '2001-01-01 00:00:10');
INSERT INTO
    chat_group(owner_id, gid, group_name, establish_time) VALUES('u1', '12345', 'name12345', '2001-01-01 00:00:15');

# 插入群组成员数据
INSERT INTO
    group_member(member_id, gid, member_auth, join_time) VALUES('u1', '10001', 'owner', '2001-01-02 00:00:00');
INSERT INTO
    group_member(member_id, gid, member_auth, join_time) VALUES('u1', '12345', 'owner', '2001-01-02 00:00:00');
INSERT INTO
    group_member(member_id, gid, member_auth, join_time) VALUES('u4', '10002', 'owner', '2001-01-02 00:00:00');
INSERT INTO
    group_member(member_id, gid, member_auth, join_time) VALUES('u2', '10001', 'member', '2001-01-02 00:00:00');
INSERT INTO
    group_member(member_id, gid, member_auth, join_time) VALUES('u3', '10001', 'member', '2001-01-02 00:00:00');
INSERT INTO
    group_member(member_id, gid, member_auth, join_time) VALUES('u4', '10001', 'member', '2001-01-02 00:00:00');
INSERT INTO
    group_member(member_id, gid, member_auth, join_time) VALUES('u5', '10002', 'member', '2001-01-02 00:00:00');
INSERT INTO
    group_member(member_id, gid, member_auth, join_time) VALUES('u6', '12345', 'member', '2001-01-02 00:00:00');