package test.freelancer.com.fltest;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * List that displays the TV Programmes
 */

public class ListFragment extends Fragment {

    private String TAG = "ListFragment";

    private int mIndex = 0;
    private int mTotalCount = 0;
    public List<String> program;
    public List<ProgramInfo> programList;
    public HashMap<String,ProgramInfo> programInfo;

    private static final String RESULTS = "results";
    private static final String COUNT = "count";
    private static final String PROGRAM_NAME = "name";
    private static final String PROGRAM_START_TIME = "start_time";
    private static final String PROGRAM_END_TIME = "end_time";
    private static final String PROGRAM_CHANNEL = "channel";
    private static final String PROGRAM_RATING = "rating";


    JSONArray prog = null;

    ExpandableListAdapter listAdapter;
    ExpandableListView expandableListView;
    SQLiteHelper dbHelper;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ListView view = (ListView) inflater.inflate(R.layout.fragment_list, container, false);

        // eurgh, damn android.os.NeworkOnMainThreadException - so pesky!
        // stackoverflow told me to do this:
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        program = new ArrayList<String>();
        programList = new ArrayList<ProgramInfo>();
        programInfo = new HashMap<String,ProgramInfo>();

        dbHelper = new SQLiteHelper(getActivity());

        //l.bello : check if network is available
        if(!isNetworkAvailable()){
            Log.d(TAG,"No network available.");
            //Populate list based on previously fetched data
            programList = dbHelper.getAll();
            Log.d(TAG, "programlist size " + programList.size());
           for(int i = 0; i <programList.size(); i++){
               program.add(i,programList.get(i).getName());
               programInfo.put(programList.get(i).getName(),programList.get(i));
           }

        } else {
            try {
                dbHelper.deleteAllPrograms();
                do {
                    // download the program guide
                    String JsonResponse = connect("http://whatsbeef.net/wabz/guide.php?start=" + mIndex);
                    JSONObject json = new JSONObject(JsonResponse);
                    //Get JSON Array results
                    prog = json.getJSONArray(RESULTS);
                    Log.d(TAG, "json array length " + prog.length());
                    mTotalCount = json.getInt(COUNT);
                    Log.d(TAG, "count " + mTotalCount);
                    for (int i = 0; i < prog.length(); i++) {
                        JSONObject p = prog.getJSONObject(i);
                        String progName = p.getString(PROGRAM_NAME);
                        program.add(progName);

                        ProgramInfo info = new ProgramInfo(progName,p.getString(PROGRAM_START_TIME),
                                p.getString(PROGRAM_END_TIME),p.getString(PROGRAM_CHANNEL),
                                p.getString(PROGRAM_RATING));

                        //add to hashmap
                        programInfo.put(progName, info);
                        //store to database
                        dbHelper.insertProgram(progName,p.getString(PROGRAM_START_TIME),p.getString(PROGRAM_END_TIME),p.getString(PROGRAM_CHANNEL),p.getString(PROGRAM_RATING));
                    }
                    mIndex += 10;
                } while (mIndex < mTotalCount);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//end of else

        expandableListView = (ExpandableListView)  inflater.inflate(R.layout.fragment_list,container,false);
        listAdapter = new ExpandableListAdapter(getActivity(),program,programInfo);
        expandableListView.setAdapter(listAdapter);

        return expandableListView;
    }

    /**
     * l.bello
     * This method checks if there is network available
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String connect(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (IOException e) {
        }
        return null;
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public class ListAdapter extends BaseAdapter {

        JSONArray array;

        public ListAdapter(JSONArray response) {
            array = response;
        }


        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public JSONObject getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            try {
                    TextView name = new TextView(getActivity());
                    name.setTextSize(40);
                    //name.setText(array.getJSONObject(position).getString("name"));
                    name.setText(programInfo.get(program.get(position)).getName());
                    layout.addView(name);

            } catch (Exception e){//(JSONException e) {
                e.printStackTrace();
            }

            return layout;
        }
    }
}


