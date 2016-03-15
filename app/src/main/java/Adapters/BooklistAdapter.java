package Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bean.BookInfo;
import vanroid.com.gdufassistant20.R;

/**
 * Created by kami on 15/10/8.
 */
public class BooklistAdapter extends ListViewAdapter{
    int id = 1;

    public BooklistAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    public View getConvertView(int position, View convertView, ViewGroup parent) {
        BookInfo b = (BookInfo) datas.get(position);
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_library,parent,false);
            holder.id = (TextView) convertView.findViewById(R.id.tv_bookinfo_id);
            holder.title = (TextView) convertView.findViewById(R.id.tv_bookinfo_title);
            holder.press = (TextView) convertView.findViewById(R.id.tv_bookinfo_press);
            holder.author = (TextView) convertView.findViewById(R.id.tv_bookinfo_author);
            holder.availableCount = (TextView) convertView.findViewById(R.id.tv_bookinfo_count);

            holder.id.setText(b.getBookId()+"");
            holder.title.setText(b.getTitle());
            holder.author.setText(b.getAuthor());
            holder.press.setText(b.getPress());
            holder.availableCount.setText(b.getBorrowableCount()+"");

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {
        TextView id;
        TextView title;
        TextView author;
        TextView press;
        TextView availableCount;
    }

}
