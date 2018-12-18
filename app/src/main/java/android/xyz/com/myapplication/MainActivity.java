package android.xyz.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.qihoo360.replugin.RePlugin;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Example of a call to a native method
        verifyStoragePermissions(this);
        tv = (TextView) findViewById(R.id.sample_text);
        LinkList list = new LinkList();
        for(int i =1;i<10;i++){
            list.add(i);
        }
//        tv.setText(list.print(list.head));
        tv.setText(stringFromJNI());

        findViewById(R.id.next_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NextPluginActivity.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.open_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJumpToPlugin(v);
            }
        });


        findViewById(R.id.download_plugin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 插件下载地址
                String urlPath = "https://raw.githubusercontent.com/xyz442/PluginDemo/master/app/src/main/res/raw/plugin.apk";
                // 插件下载后的存放路径
                String downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath();

                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.putExtra("urlPath", urlPath);
                intent.putExtra("downloadDir", downloadDir);
                startService(intent);
            }
        });
    }

    public void onJumpToPlugin(View view) {
        // 打开一个插件的Activity
        RePlugin.startActivity(MainActivity.this,
                RePlugin.createIntent("plugin", "com.xyz.plugindemo.MainActivity"));

    }

    public class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode Merge(ListNode list1, ListNode list2) {
        if (list1 == null) return list2; //判断到某个链表为空就返回另一个链表。如果两个链表都为空呢？没关系，这时候随便返回哪个链表，不也是空的吗?
        if (list2 == null) return list1;
        ListNode list0 = null;//定义一个链表作为返回值
        if (list1.val < list2.val) {//判断此时的值，如果list1比较小，就先把list1赋值给list0，反之亦然
            list0 = list1;
            list0.next = Merge(list1.next, list2);//做递归，求链表的下一跳的值
        } else {
            list0 = list2;
            list0.next = Merge(list1, list2.next);
        }
        return list0;
    }

    public ListNode Merge2(ListNode list1, ListNode list2) {
        if (list1 == null)
            return list2;
        if (list2 == null)
            return list1;
        ListNode tmp1 = list1;
        ListNode tmp2 = list2;
        ListNode head = new ListNode(0);//这里不能把返回链表赋值为null，因为下一行马上就要把它赋值给另一链表，得让它在内存里有位置才行
        ListNode headptr = head;
        while (tmp1 != null && tmp2 != null) {

            if (tmp1.val <= tmp2.val) {
                head.next = tmp1;
                head = head.next;
                tmp1 = tmp1.next;
            } else {
                head.next = tmp2;
                head = head.next;
                tmp2 = tmp2.next;
            }
        }
        //其中一个链表已经跑到头之后，继续单链表的合并
        while (tmp1 != null) {
            head.next = tmp1;
            head = head.next;
            tmp1 = tmp1.next;
        }
        while (tmp2 != null) {
            head.next = tmp2;
            head = head.next;
            tmp2 = tmp2.next;
        }
        head = headptr.next;
        return head;

    }


    public class LinkList{//链表
        public Node head;//对第一个节点的引用
        public Node current;//对下一个节点的引用

        public void add(Object data){//初始化每个节点
            if(head == null){//如果链表结构不存在
                head = new Node(data);//初始化第一个节点
                current = head;//引用指向本节点在只有一个节点的情况下
            }else{
                current.next = new Node(data);//把本节点类中的成员变量next设为对下一个节点的引用
                current = current.next;//指向下一个节点
            }
            System.out.print(data+" ");
        }

        // 排序输出
        public StringBuilder print(Node node){
            StringBuilder stringBuilder = new StringBuilder();
            if(node == null){
                return stringBuilder ;
            }

            while(node != null){
                stringBuilder.append(node.data+" ");
                node = node.next;//最后一个Node的next没有new 所以为null
            }
            return stringBuilder;
        }


        // 定位索引的位置
        public void posIndex(int index) {
            if (head == null) {
                return;
            }

            if (index == -1) {
                return;
            }

            current = head;
            int j = 1;
            while (current != null && j < index) {
                current = current.next;
                j++;
            }
        }


        class Node{//链表中每个节点的数据存储类
            Object data;//数据域
            Node next;//下一个指针

            public Node(Object data){
                this.data = data;
            }
        }


    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    /**
     * 动态申请读写权限
     **/
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
