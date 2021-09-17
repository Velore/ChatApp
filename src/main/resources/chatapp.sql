CREATE DATABASE chatapp CHARACTER SET utf8;

CREATE TABLE chatuser(
    uid VARCHAR(10) NOT NULL PRIMARY KEY,
    uname VARCHAR(10),
    pwd VARCHAR(16) NOT NULL
);
CREATE TABLE chatgroup(
    #the owner of the group
    invite_id VARCHAR(10) NOT NULL,
    FOREIGN KEY (invite_id) REFERENCES chatuser(uid),
    gid VARCHAR(10) NOT NULL PRIMARY KEY
);
CREATE TABLE chatmessage(
    send_time TIMESTAMP NOT NULL,
    uid VARCHAR(10) NOT NULL,
    FOREIGN KEY (uid) REFERENCES chatuser(uid),
    gid VARCHAR(10) NOT NULL,
    FOREIGN KEY (gid) REFERENCES chatgroup(gid),
    msg_str VARCHAR(100) NOT NULL
);
CREATE TABLE chatfriend(
    #invite to be friend
    invite_id VARCHAR(10) NOT NULL,
    #accept to be friend
    accept_id VARCHAR(10) NOT NULL,
    gid VARCHAR(10) NOT NULL,
    FOREIGN KEY (gid) REFERENCES chatgroup(gid)
);