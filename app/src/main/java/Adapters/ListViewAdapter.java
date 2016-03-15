package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class ListViewAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> datas;
	protected LayoutInflater mInflater;
	
	public ListViewAdapter(Context context,List<T> datas) {
		this.context = context;
		this.datas = datas;
		mInflater = LayoutInflater.from(context);
	}


	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup paraent) {
		view = getConvertView(position,view,paraent);
		return view;
	}
	
	//把设置ListView样式的方法给子类实现
	public abstract View getConvertView(int position,View convertView,ViewGroup parent);

}
