package com.xinlan.myyanwords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WordsDao mDao;
    private List<Word> wordList;

    private ListView mListView;
    private DataAdapter mDapter;

    private LayoutInflater mInflater;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInflater = LayoutInflater.from(this);
        mListView = (ListView) findViewById(R.id.list);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("");

        mDao = new WordsDao(this);
        try {
            wordList = mDao.getWordsList();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mDapter = new DataAdapter();
        mListView.setAdapter(mDapter);
    }

    private final class DataAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return wordList.size();
        }

        @Override
        public Word getItem(int i) {
            return wordList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = mInflater.inflate(R.layout.view_item, null);

                ViewHolder holder = new ViewHolder();
                holder.wordText = (TextView) view.findViewById(R.id.word);
                holder.translateText = (TextView) view.findViewById(R.id.translate);

                view.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) view.getTag();
            Word word = getItem(pos);
            holder.wordText.setText(word.getWord());
            holder.translateText.setText(word.getTranslate());
            return view;
        }
    }

    public static class ViewHolder {
        TextView wordText;
        TextView translateText;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDao.close();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_resort:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }
}//end class
