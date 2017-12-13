package com.tylz.wechat.model.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author cxw
 * @date 2017/12/5
 * @des 实现Parcelable步骤
 * 1）implements Parcelable
 * 2）重写writeToParcel方法，将你的对象序列化为一个Parcel对象，即：将类的数据写入外部提供的Parcel中，打包需要传递的数据到Parcel容器保存，以便从 Parcel容器获取数据
 * 3）重写describeContents方法，内容接口描述，默认返回0就可以
 * 4）实例化静态内部对象CREATOR实现接口Parcelable.Creator
 */

public class TestMessage implements Parcelable {
    private int age;

    private TestMessage(Parcel in) {
        age = in.readInt();
    }

    /**
     * 内容描述接口，基本不用管
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 写入接口函数，打包
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
    }

    public static final Parcelable.Creator<TestMessage> CREATOR = new Parcelable.Creator<TestMessage>() {
        @Override
        public TestMessage createFromParcel(Parcel source) {
            return new TestMessage(source);
        }

        @Override
        public TestMessage[] newArray(int size) {
            return new TestMessage[size];
        }
    };

}
