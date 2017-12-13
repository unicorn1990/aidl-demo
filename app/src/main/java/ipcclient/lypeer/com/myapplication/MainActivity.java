package ipcclient.lypeer.com.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ipcclient.lypeer.com.Book;
import ipcclient.lypeer.com.BookManager;

public class MainActivity extends AppCompatActivity {

    private BookManager mBookManager;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;

    //包含Book对象的list
    private List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setClass(this,MyService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(getLocalClassName(), "service connected");
            mBookManager = BookManager.Stub.asInterface(service);
            mBound = true;

            if (mBookManager != null) {
                try {
                    mBooks = mBookManager.getBooks();
                    Log.e(getLocalClassName(), mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getLocalClassName(), "service disconnected");
            mBound = false;
        }
    };

    public void onclick1(View view) {

        try {
            mBookManager.addBook(new Book("天马书",100));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            Log.e(getLocalClassName(), mBookManager.getBooks().toString());
            Toast.makeText(this, books2String(mBookManager.getBooks()), Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    private String books2String(List<Book> books) {
        StringBuilder sb=new StringBuilder();
        for(Book book:books)
        {
            sb.append(book.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
