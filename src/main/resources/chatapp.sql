CREATE DATABASE chatapp CHARACTER SET utf8;

CREATE TABLE chatuser(
    # 用户id
    uid VARCHAR(10) NOT NULL PRIMARY KEY,
    # 用户名字
    uname VARCHAR(10),
    # 用户密码
    pwd VARCHAR(16) NOT NULL
);
CREATE TABLE chatgroup(
    # 群主id
    owner_id VARCHAR(10) NOT NULL,
    # 群组id
    gid VARCHAR(10) NOT NULL PRIMARY KEY,
    # 建群时间
    establish_time TIMESTAMP NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES chatuser(uid)
);
CREATE TABLE authority(
    # 成员id
    member_id VARCHAR(10) NOT NULL,
    # 群组id
    gid VARCHAR(10) NOT NULL,
    # 成员在群组的权限
    auth VARCHAR(5) NOT NULL,
    # 加入群组的时间
    join_time TIMESTAMP NOT NULL
);
CREATE TABLE chatmessage(
    # 消息发送时间
    send_time TIMESTAMP NOT NULL,
    # 消息发送者id
    sender_id VARCHAR(10) NOT NULL,
    # 消息发送的群组id
    gid VARCHAR(10) NOT NULL,
    # 消息的内容
    msg_str VARCHAR(100) NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES chatuser(uid),
    FOREIGN KEY (gid) REFERENCES chatgroup(gid)
);
CREATE TABLE attention(
    #关注者id
    follower_id VARCHAR(10) NOT NULL,
    #被关注者id
    accept_id VARCHAR(10) NOT NULL,
    #关注的时间
    follow_time TIMESTAMP NOT NULL
);