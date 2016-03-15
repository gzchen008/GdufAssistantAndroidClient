package vanroid.com.gdufassistant20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import vanroid.com.gdufassistant20.R;

public class FileBrowserActivity extends Activity {

	private String[] fl;
	private ListView filelist;
	private ArrayAdapter<String> mAdapter;
	private final String[][] MIME_MapTable={{".doc","application/msword"}, 
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, 
            {".xls","application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_file_browser);
		
		findViewById(R.id.ll_file_back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myDownload");
		if(!file.exists()&&!file.isDirectory()){
			file.mkdir();
		}
		fl = file.list();
		
		filelist = (ListView) findViewById(R.id.fileList);
		
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fl);
		
		filelist.setAdapter(mAdapter);
		
		filelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				try {
					openFile(fl[position]);
				} catch (Exception e) {
					Toast.makeText(FileBrowserActivity.this, "没有安装响应的应用哦！", Toast.LENGTH_SHORT).show();
				}
					
			}
		});
		
	}
	
	private void openFile(String filename){ 
	    
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myDownload/"+filename);
		
	    Intent intent = new Intent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    //设置intent的Action属性 
	    intent.setAction(Intent.ACTION_VIEW); 
	    //获取文件file的MIME类型 
	    String type = getMIMEType(file); 
	    //设置intent的data和Type属性。 
	    intent.setDataAndType(Uri.fromFile(file), type);
	    //跳转 
	    startActivity(intent); 
	    
	}
	
	private String getMIMEType(File file) { 
	     
	    String type="*/*"; 
	    String fName = file.getName(); 
	    //获取后缀名前的分隔符"."在fName中的位置。 
	    int dotIndex = fName.lastIndexOf("."); 
	    if(dotIndex < 0){ 
	        return type; 
	    } 
	    /* 获取文件的后缀名*/ 
	    String end=fName.substring(dotIndex,fName.length()).toLowerCase(); 
	    if(end=="")return type; 
	    //在MIME和文件类型的匹配表中找到对应的MIME类型。 
	    for(int i=0;i<MIME_MapTable.length;i++){
	        if(end.equals(MIME_MapTable[i][0])) 
	            type = MIME_MapTable[i][1]; 
	    }        
	    return type; 
	} 
	
}
