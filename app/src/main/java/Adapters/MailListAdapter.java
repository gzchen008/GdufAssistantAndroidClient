package Adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bean.MailInfo;
import vanroid.com.gdufassistant20.R;

/**
 * Created by kami on 15/9/10.
 */
public class MailListAdapter<M> extends ListViewAdapter {

    private int[] mColor = new int[]{0xff00bfff,0xfffaebd7,0xffffb6c1,0xffff9800,0xff90ee90,0xffffb6c1,0xffdda0dd,0xffd8bfd8,0xfff5deb3,0xffee82ee};

    public MailListAdapter(Context context, List datas) {
        super(context, datas);
    }



    @Override
    public View getConvertView(int position, View convertView, ViewGroup parent) {
        MailInfo mail = (MailInfo) datas.get(position);
        ViewHolder holder = null;
	//使用convertView之前先判断是否为空（即之前有没有出现过），如果为空，这对这个Item进行初始化并绑定Holder
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_mail,parent,false);
            holder.addresser = (TextView) convertView.findViewById(R.id.tv_addresser);
            holder.firstWorld = (TextView) convertView.findViewById(R.id.tv_first_word);
            holder.title = (TextView) convertView.findViewById(R.id.tv_mail_title);
            holder.time = (TextView) convertView.findViewById(R.id.tv_mail_date);
            holder.size = (TextView) convertView.findViewById(R.id.tv_mail_size);
            holder.readFlag = (ImageView) convertView.findViewById(R.id.icon_is_read);
            holder.hasAttach = (ImageView) convertView.findViewById(R.id.icon_has_attach);
            convertView.setTag(holder);
        }else {
	//如果不为空则根据Holder取得这个Item，实现View的复用，减少创建ConverView的次数还有FindViewById的次数
            holder = (ViewHolder) convertView.getTag();
        }
        holder.addresser.setText(mail.getSender());
        holder.firstWorld.setText(mail.getSender().substring(0, 1));
        GradientDrawable sd = (GradientDrawable) holder.firstWorld.getBackground();
        sd.setColor(mColor[position%mColor.length]);
        holder.title.setText(mail.getTitle());
        holder.size.setText("size:"+mail.getSize());
        holder.time.setText(mail.getDate());

        if (mail.getReadFlag()==0){
            holder.readFlag.setVisibility(View.GONE);
        }else {
            holder.readFlag.setVisibility(View.VISIBLE);
        }

        if (mail.isAttach()){
            holder.hasAttach.setVisibility(View.VISIBLE);
        }else {
            holder.hasAttach.setVisibility(View.GONE);
        }

        return convertView;
    }

    // 邮件列表的内容
    class ViewHolder {
        TextView firstWorld;
        TextView title;
        TextView addresser;
        TextView size;
        TextView time;
        ImageView readFlag;
        ImageView hasAttach;
    }


}
