package test.freelancer.com.fltest;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Android 18 on 8/7/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<String> program;
    HashMap<String,ProgramInfo> programInfoHashMap;

    public ExpandableListAdapter(Context c, List<String> header, HashMap<String,ProgramInfo> headerInfo){
        this.context = c;
        this.program = header;
        this.programInfoHashMap = headerInfo;
    }

    @Override
    public int getGroupCount() {
        return program.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return program.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return programInfoHashMap.get(program.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_parent, null);
        }

        TextView header = (TextView) convertView
                .findViewById(R.id.programHeader);
        header.setTypeface(null, Typeface.BOLD);
        header.setTextSize(30);
        header.setText(program.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child,null);
        }

        TextView startTime = (TextView) convertView.findViewById(R.id.startTime);
        startTime.setTextSize(20);
        startTime.setText("Start Time: " + ((ProgramInfo)getChild(groupPosition,childPosition)).getStartTime());

        TextView endTime = (TextView) convertView.findViewById(R.id.endTime);
        endTime.setTextSize(20);
        endTime.setText("End Time: " + ((ProgramInfo)getChild(groupPosition,childPosition)).getEndTime());

        TextView channel = (TextView) convertView.findViewById(R.id.channel);
        channel.setTextSize(20);
        channel.setText("Channel: " + ((ProgramInfo)getChild(groupPosition,childPosition)).getChannel());

        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        rating.setTextSize(20);
        rating.setText("Rating: " + ((ProgramInfo)getChild(groupPosition,childPosition)).getRating());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
