package Utils;

import Po.Common.Group;
import Po.Common.Message.ChatMessage;
import Po.Common.User;

import java.io.*;
import java.util.ArrayList;

/**
 * 持久化工具类
 * 包括从文件中读取数据read，将数据写入文件write
 * @author chenzhuohong
 */
public class StorageUtils {

    /**
     * 从对应路径下的文件中读取对象
     * @param filePath 文件路径
     * @return 读取出的对象列表
     */
    public static ArrayList<Object> read(String filePath){
        File file = new File(filePath);
        ArrayList<Object> objList = new ArrayList<>();
        try(
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))
        ){
//            while(ois.available()!=0){
//                Object o = ois.readObject();
//                System.out.println(o.toString());
//                objList.add(o);
//            }
            //只能强转
            objList = (ArrayList<Object>) ois.readObject();
        }catch (EOFException e){
            //读取结束的标志
        }catch (IOException | ClassNotFoundException e1){
            e1.printStackTrace();
        }
        return objList;
    }

    /**
     * 将对象列表写入路径下的对应文件
     * @param objList 要写入的对象列表
     * @param filePath 文件路径
     * @param isAppend 是否在文件末尾写入
     */
    public static void write(ArrayList<Object> objList, String filePath, boolean isAppend){
        File file = new File(filePath);
        try(
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, isAppend))
        ){
//            for(Object o : objList){
//                oos.writeObject(o.toString()+"\n");
//                oos.flush();
//            }
            //不能一个个写，否则读不出来
            oos.writeObject(objList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 把Object列表转换为user列表
     */
    public static ArrayList<User> objToUser(ArrayList<Object> objList){
        ArrayList<User> userList = new ArrayList<>();
        for(Object o : objList){
            userList.add((User) o);
        }
        return userList;
    }

    /**
     * 把user列表转换为Object列表
     */
    public static ArrayList<Object> userToObj(ArrayList<User> userList){
        return new ArrayList<>(userList);
    }

    /**
     * 把Object列表转换为group列表
     */
    public static ArrayList<Group> objToGroup(ArrayList<Object> objList){
        ArrayList<Group> groupList = new ArrayList<>();
        for(Object o : objList){
            groupList.add((Group) o);
        }
        return groupList;
    }

    /**
     * 把group列表转换为Object列表
     */
    public static ArrayList<Object> groupToObj(ArrayList<Group> groupList){
        return new ArrayList<>(groupList);
    }

    /**
     * 把Object列表转换为chatMessage列表
     */
    public static ArrayList<ChatMessage> objToMsg(ArrayList<Object> objList){
        ArrayList<ChatMessage> msgList = new ArrayList<>();
        for(Object o : objList){
            msgList.add((ChatMessage) o);
        }
        return msgList;
    }

    /**
     * 把chatMessage列表转换成Object列表
     */
    public static ArrayList<Object> msgToObj(ArrayList<ChatMessage> msgList){
        return new ArrayList<>(msgList);
    }
}
