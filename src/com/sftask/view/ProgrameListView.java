package com.sftask.view;

import java.util.ArrayList;
import java.util.List;

import com.sftask.R;
import com.sftask.model.Programe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgrameListView extends BaseAdapter {
	private List<Programe> data=new ArrayList<Programe>();
	private Context context;
	public ProgrameListView(List<Programe> data,Context context){
		this.data=data;
		this.context=context;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int i) {
		return data.get(i);
	}

	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int i, View convertView, ViewGroup viewgroup) {
		ViewHolder holder;  
        if(convertView == null)  
        {    
        	LayoutInflater la= LayoutInflater.from(context);  
            convertView=la.inflate(R.layout.programe_list_view, null);  
              
            holder = new ViewHolder();  
            holder.imgage=(ImageView) convertView.findViewById(R.id.image);  
            holder.text = (TextView) convertView.findViewById(R.id.text);  
              
            convertView.setTag(holder);  
        }else{  
            holder = (ViewHolder) convertView.getTag();  
        }  
         final Programe pr = (Programe)data.get(i);  
        //设置图标   
        holder.imgage.setImageDrawable(pr.getIcon());  
        //设置程序名   
        holder.text.setText(pr.getName());  
          
        return convertView;  
	}
	class ViewHolder{  
	     TextView text;  
	    ImageView imgage;  
	}  

}
