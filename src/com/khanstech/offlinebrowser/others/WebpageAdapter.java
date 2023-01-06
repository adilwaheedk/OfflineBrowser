package com.khanstech.offlinebrowser.others;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.khanstech.offlinebrowser.R;
import com.khanstech.offlinebrowser.activity.ActivitySaveList;
import com.khanstech.offlinebrowser.database.Webpage;

public class WebpageAdapter extends BaseAdapter {

	public static boolean clickable;
	private List<Webpage> list;
	private LayoutInflater inflater;
	private ActivitySaveList parentAct;
	private SparseBooleanArray selection;

	public WebpageAdapter(ActivitySaveList parentAct, List<Webpage> list) {
		this.parentAct = parentAct;
		this.list = list;
		selection = new SparseBooleanArray();
		clickable = false;
		inflater = (LayoutInflater) parentAct
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void addItem(Object item) {
		list.add((Webpage) item);
		notifyDataSetChanged();
	}

	public void removeItem(Object item) {
		list.remove(item);
		notifyDataSetChanged();
	}

	public boolean hasStableIds() {
		return true;
	}

	public void setNewSelection(int position, boolean value) {
		selection.put(position, value);
		notifyDataSetChanged();
	}

	public void setAllSelection(boolean value) {
		selection = new SparseBooleanArray();
		for (int i = 0; i < getCount(); i++)
			selection.put(i, value);
		notifyDataSetChanged();
	}

	public boolean isPositionChecked(int position) {
		return selection.get(position, false);
	}

	public List<Integer> getCurrentCheckedPosition() {
		List<Integer> checkedPos = new ArrayList<Integer>();
		for (int i = 0; i < getCount(); i++) {
			if (isPositionChecked(i))
				checkedPos.add(i);
		}
		return checkedPos;
	}

	public void removeSelection(int position) {
		selection.delete(position);
		notifyDataSetChanged();
	}

	public void clearSelection() {
		selection = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.list_webpage, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		// convertView.setOnLongClickListener(new OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View v) {
		// holder.offline.setVisibility(View.GONE);
		// holder.online.setVisibility(View.GONE);
		// holder.links.setVisibility(View.GONE);
		//
		// return true;
		// }
		// });

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickable) {
					if (!isPositionChecked(position))
						setNewSelection(position, true);
					else
						removeSelection(position);
					int rows = ActionBarCallBack.rows = getCurrentCheckedPosition()
							.size();
					String title = rows < 2 ? " Item " : " Items ";
					ActionBarCallBack.mode.setSubtitle(rows + title
							+ "Selected");
				}
			}
		});

		final Webpage wp = list.get(position);
		holder.title.setText(wp.getTitle());
		holder.url.setText(wp.getUrl());
		if (!clickable) {
			holder.offline.setVisibility(View.VISIBLE);
			holder.online.setVisibility(View.VISIBLE);
			holder.links.setVisibility(View.VISIBLE);
			holder.offline.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					parentAct.loadPage(wp, false);
				}
			});

			holder.online.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					parentAct.loadPage(wp, true);
				}
			});

			holder.links.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					parentAct.loadLinks(wp);
				}
			});
		} else {
			holder.offline.setVisibility(View.INVISIBLE);
			holder.online.setVisibility(View.INVISIBLE);
			holder.links.setVisibility(View.INVISIBLE);
		}

		if (clickable & isPositionChecked(position))
			convertView.setBackgroundColor(parentAct.getResources().getColor(
					android.R.color.holo_blue_light));
		else
			convertView.setBackgroundColor(parentAct.getResources().getColor(
					android.R.color.transparent));

		return convertView;
	}

	private static class ViewHolder {
		TextView title, url;
		ImageButton offline, online, links;

		ViewHolder(View v) {
			title = (TextView) v.findViewById(R.id.webpage_title);
			url = (TextView) v.findViewById(R.id.webpage_url);
			offline = (ImageButton) v.findViewById(R.id.webpage_offline);
			online = (ImageButton) v.findViewById(R.id.webpage_online);
			links = (ImageButton) v.findViewById(R.id.webpage_list_links);
		}
	}
}