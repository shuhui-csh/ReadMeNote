/**
 * 
 */
package com.readme.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;

import com.example.readmenote.R;
import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeConstants;
import com.readme.client.ReadMeException;
import com.readme.data.Note;
import com.readme.data.Resource;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author zibin
 *
 */
public class AddNoteActivtiyDemo extends Activity {
	EditText addNoteEdt ;
	Button createNoteBtn;
	Button getNoteBtn;
	Note note;
	EditText addNoteTitleEdt;
	Resource resource;
	private String content = "测试自定义标签：<br><h1><mxgsa>测试自定义标签</mxgsa></h1>";
	private StringBuilder contentBuilder = new StringBuilder();
	File file = new File("/sdcard/3.png");
	File file2 = new File("/sdcard/3.png");
	 // YNote online environment
    private static final OAuthServiceProvider SERVICE_PROVIDER =
        new OAuthServiceProvider(ReadMeConstants.REQUEST_TOKEN_URL,
        		ReadMeConstants.USER_AUTHORIZATION_URL,
        		ReadMeConstants.ACCESS_TOKEN_URL);

	private static final String CONSUMER_KEY = ReadMeConstants.CONSUMER_KEY;
	private static final String CONSUMER_SECRET = ReadMeConstants.CONSUMER_SECRET;

	private static final OAuthConsumer CONSUMER = new OAuthConsumer(null,
			CONSUMER_KEY, CONSUMER_SECRET, SERVICE_PROVIDER);

	private static ReadMeClient client = new ReadMeClient(CONSUMER);
	public final String NEWS = "<p>　　环比全部停涨首次出现</p>\r\n<p>　　数据显示，在新建商品住宅方面，2012年1月全国70个大中城市，价格环比下降的城市有48个，持平的城市有22个，没有一个城市出现上涨。从房价价格指数公布来看，首次出现了新建商品住宅环比全部停涨的现象。</p>\r\n<p>　　从同比看，70个大中城市中，价格下降的城市有15个，比去年12月份增加6个。1月份，同比涨幅回落的城市有50个，涨幅均未超过3.9%。</p>\r\n<p>　　二手住宅</p>\r\n<p>　　仅有5个城市环比上涨</p>\r\n<p>　　从二手房看,与上月相比，70个大中城市中，价格下降的城市有54个，持平的城市有11个。与去年12月份相比，1月份环比价格下降的城市增加了3个。环比上涨的仅5个城市：分别为贵阳、济宁、襄阳、韶关、遵义，均仅上涨0.1%。同比看，70个大中城市中，价格下降的城市有37个，比去年12月份增加了8个。1月份，同比涨幅回落的城市有29个，涨幅均未超过3.5%。</p>\r\n<p>　　北京情况</p>\r\n<p>　　二手房环比同比均下跌</p>\r\n<p>　　数据显示，北京二手房价格不管是环比还是同比，都在下跌，且下跌幅度均有所加大。</p>\r\n<p>　　在环比方面，自去年8月份出现停涨后，二手房价格环比开始下跌，且此后每个月的下跌幅度在不断加大，到2012年1月份，其环比下跌幅度已达到0.9%。而在同比方面，2012年1月份下跌3.1%，创下最大跌幅。</p>\r\n<p>　　北京新建住宅价格指数2010年5月时同比涨幅为22%，而到了2012年1月同比涨幅仅为0.1%。新建商品房价格指数同比涨幅也是连续下跌，下降幅度也比较大，去年年底同比涨幅为1.3%，而到了今年1月份，则同比涨幅下跌为0.1%。在环比方面，继2011年10月首次出现下跌后，环比继续下跌为0.1%。</p>\r\n<p>　　■ 分析</p>\r\n<p>　　北京房价调控成效明显</p>\r\n<p>　　北京中原地产市场总监张大伟认为，北京作为限购执行最严格的城市，房价调控已经见到明显成效。限购导致的直接需求减少，限购抑制投资、投机，在北京出台的最严格版限购下，5年外地户籍限购年限使得购房者回归自住，本地需求占据9成，自住首套占据9成，全市最近一年成交量中投资及投机基本绝迹。</p>\r\n<p>　　同时，买卖双方博弈加剧，限购限贷使得购房者期待价格下调，但是投资手段匮乏、通货膨胀依然是阻碍价格下滑的关键因素。特别是城区部分二手房房主依然坚挺价格，惜售，这使得在价格依然居高不下的情况下，购房者入市谨慎。</p>\r\n<p>　　■ 预测</p>\r\n<p>　　今年房价或下调10%-20%</p>\r\n<p>　　张大伟认为，2012年限购政策不会放松，一线城市楼市拐点已经明确，预期在6-12个月内房价可能还有10%-20%的下调，而且一线城市对全国的示范作用非常大，不仅在政策执行力度上，在房价下调过程中也会明显影响全国。</p>\r\n<p>　　链家地产市场研究部冯联联认为，在楼市调控效果继续巩固的背景下，降价趋势仍会持续。1月份全国多个城市新房市场成交跌入谷底，观望情绪依旧浓重，为加速销售回款，将不断有开发商加入降价换量阵营，新房价格预期会进一步下调。</p>";
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.add_note_demo);
			 client.setAccessToken("e2c9c4a1519b86913372bc9bb752f310", "3a33324e2e360266b1dd82ef897091e7");
			addNoteEdt = (EditText)findViewById(R.id.add_note_content);
			createNoteBtn = (Button)findViewById(R.id.creat_note_btn);
			getNoteBtn = (Button)findViewById(R.id.get_note_btn);
			addNoteTitleEdt = (EditText)findViewById(R.id.add_note_title);
//			addNoteEdt.setText(Html.fromHtml(NEWS));
			
			createNoteBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AsynWork asynWork =  new AsynWork();
					asynWork.execute("");
					
					int index = addNoteEdt.getSelectionStart();
					Editable editable = addNoteEdt.getText();


				}
			});
			
			getNoteBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {				
					try {
						Note nt = getNote(note.getPath());
						String getNote = nt.getContent();
						System.out.println(getNote);
						addNoteEdt.setText(Html.fromHtml(getNote));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			
		}
		/**
		 *    <head>
		 *    <title>文档的标题</title>
		 *    </head>
		 *    <body>
		 *    文档的内容... ...
		 *    </body>
		 *    	<h1>这是标题 1</h1>
		 *	   	<h2>这是标题 2</h2>
		 *		<h3>这是标题 3</h3>
		 *		<h4>这是标题 4</h4>
		 *		<h5>这是标题 5</h5>
		 *		<h6>这是标题 6</h6>
		 *    <img src="/i/eg_tulip.jpg"  alt="上海鲜花港 - 郁金香" />
		 *    <p>This is some text in a very short paragraph</p>
		 * <br>	定义简单的折行。<br> 标签没有结束标签。
		 * <h1> to <h6>	定义 HTML 标题。    
		 * <p>	定义段落。
		 * <img>	定义图像。
		 * <body>	定义文档的主体。
		 * 
		 * */
		private String  addTitle(String tilte){
			StringBuilder sb =new StringBuilder();
			sb.append("<h3>");
			sb.append(tilte);
			sb.append("</h3>");
			return sb.toString();
		}
		
		private String addDetailContent(String content){
			StringBuilder sb = new StringBuilder();
			sb.append("<br>");
			sb.append("<p>	");
			sb.append(content);
			sb.append("</p>");
			return sb.toString();
		}
		
		private String addContent(String content){
			contentBuilder.append(content);
			return contentBuilder.toString();
		}
		
	    private  Note createNote(final String notebookPath) throws Exception {
	        final Note note = new Note();
	        note.setAuthor("rhw");
	        note.setSize(100);
	        note.setSource("");
	        note.setTitle(addNoteTitleEdt.getText().toString());	     
	        String content = addContent(addTitle(addNoteTitleEdt.getText().toString()));
	        content = addContent(addDetailContent(addNoteEdt.getText().toString()));        
	        content = addContent(resource.toResourceTag());
	        System.out.println(content); 
	        addNoteEdt.setText(Html.fromHtml(content));
	        note.setContent(content);
	        try {
	            return client.createNote(notebookPath, note);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	Toast.makeText(getApplicationContext(), "-----我错了",Toast.LENGTH_SHORT).show();
	            	return null;
	            } else {
	                throw e;
	            }
	        }
	    }
	    private static Note getNote(final String notePath) throws Exception {
	        try {
	            return client.getNote(notePath);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            	return null;
	            } else {
	                throw e;
	            }
	        }
	    }
	    
	    private static void updateNote(final Note note)
	            throws Exception {
	        try {
	            client.updateNote(note);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            	
	            } else {
	                throw e;
	            }
	        }
	    }
	    
	    private static void deleteNote(final String notePath) throws Exception {
	        try {
	            client.deleteNote(notePath);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            } else {
	                throw e;
	            }
	        }
	    }
	    
	    public static Resource uploadResource(final File resource) throws Exception {
	        try {
	            return client.uploadResource(resource);
	        } catch (ReadMeException e) {
	            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
	            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
	            	return null;
	            } else {
	                throw e;
	            }
	        }
	    }
	    

	    
	    private static byte[] toBytes(InputStream input) throws IOException {
	        try {
	            ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
	            byte[] buffer = new byte[1024];
	            int n = -1;
	            while ((n = input.read(buffer)) != -1) {
	                bytes.write(buffer, 0, n);
	            }
	            bytes.close();
	            return bytes.toByteArray();
	        } finally {
	            input.close();
	        }
	    }
	    
	    private class AsynWork extends AsyncTask{ 	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Object result) {
				try {
					note = createNote(null);
					System.out.println(note);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onPostExecute(result);

			}

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				try {
					resource =   uploadResource(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
	    	
	    }
	    
}
