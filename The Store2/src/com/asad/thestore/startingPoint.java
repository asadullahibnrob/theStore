package com.asad.thestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class startingPoint extends Activity implements OnItemClickListener {
	/** Called when the activity is first created. */
	ArrayList<Models> modelList;
	StoreAdapter adapter;
     public static String download = null;
     TextView title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		
		new JSONAsyncTask().execute("https://devs.ouya.tv/api/v1/apps");

		initialize();

	}

	

	private void initialize() {
		modelList = new ArrayList<Models>();
		ListView listview = (ListView) findViewById(R.id.list);
		adapter = new StoreAdapter(getApplicationContext(), R.layout.row,
				modelList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(startingPoint.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting server");
			dialog.show();
			dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(String... urls) {
			try {

				// ------------------>>
				HttpGet httppost = new HttpGet(urls[0]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				// StatusLine stat = response.getStatusLine();
				int status = response.getStatusLine().getStatusCode();

				if (status == 200) {
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);

					JSONObject jsono = new JSONObject(data);
					JSONArray jarray = jsono.getJSONArray("apps");

					for (int i = 0; i < jarray.length(); i++) {

						JSONObject object = jarray.getJSONObject(i);

						Models model = new Models();
						model.setName(object.getString("title"));
						model.setImage(object.getString("main_image_full_url"));
						model.setUuid(object.getString("uuid"));
						model.setTags(object.getString("tags"));
						model.setVersion(object.getString("version"));
						modelList.add(model);

					}
					return true;
				}

				// ------------------>>

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			dialog.cancel();
			adapter.notifyDataSetChanged();
			if (result == false)
				Toast.makeText(getApplicationContext(),
						"Unable to fetch data from server", Toast.LENGTH_LONG)
						.show();

		}
	}

	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		String data = modelList.get(position).getName().toString()
				.replaceAll("", "").replace(" ", "-");
		String aUuid = modelList.get(position).getUuid().toString();
		String infoUrl = ("https://devs.ouya.tv/api/v1/apps/" + aUuid + "/download");
        download = new String (infoUrl);
		new getJsonUrlTask().execute(infoUrl);

	}
	
	class getJsonUrlTask extends AsyncTask<String, Void, String> {
        String downloadUrlResults;
        private void getJsonUrlTask(String MyResults){
            this.downloadUrlResults = MyResults;
        }
        protected String doInBackground(String... urls) {
            InputStream is = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpPost = new HttpGet(urls[0]);
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");

                String resString = sb.toString();

                is.close();
                return resString;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
        	title = (TextView)findViewById(R.id.textView1);
            downloadUrlResults = result;
            downloadApk(downloadUrlResults);
        }
    }
	
	private String getLink(String urlResults){
        try{
            JSONObject MyResults = new JSONObject(urlResults);
            String url = MyResults.getJSONObject("app").getString("downloadLink");
            return url;
        }
        catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
	
	
	 private void downloadApk(String urlResults)
	    {
	        String link = getLink(urlResults);
	        Intent i = new Intent(Intent.ACTION_VIEW);
	        i.setData(Uri.parse(link));
	        startActivity(i);
	    }
	
}