package com.khanstech.offlinebrowser.others;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import com.khanstech.offlinebrowser.R;
import com.khanstech.offlinebrowser.activity.ActivitySaveList;
import com.khanstech.offlinebrowser.database.Webpage;
import com.khanstech.offlinebrowser.database.WebpageCRUD;
import com.khanstech.offlinebrowser.webview.ArchiveHelper;

public class ActionBarCallBack implements ActionMode.Callback {

	private WebpageAdapter adapter;
	private ActivitySaveList parentAct;
	private boolean selectall = false;
	public static int rows = 0;
	public static ActionMode mode;

	public ActionBarCallBack(ActivitySaveList parentAct, WebpageAdapter adapter) {
		this.adapter = adapter;
		this.parentAct = parentAct;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_delete_multiple:
			deleteSelectedItems(parentAct);
			rows = 0;
			adapter.clearSelection();
			mode.finish();
			return true;
		case R.id.action_select_all:
			selectall = !selectall;
			if (selectall) {
				adapter.setAllSelection(true);
				rows = adapter.getCurrentCheckedPosition().size();
			} else {
				adapter.clearSelection();
				rows = adapter.getCurrentCheckedPosition().size();
			}
			String title = rows < 2 ? " Item " : " Items ";
			mode.setSubtitle(rows + title + "Selected");
		default:
			return false;
		}
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		WebpageAdapter.clickable = true;
		adapter.clearSelection();
		rows = 0;
		ActionBarCallBack.mode = mode;
		mode.getMenuInflater().inflate(R.menu.menu_savelist_cab, menu);
		mode.setTitle("Select Items");
		mode.setSubtitle(rows + " Item Selected");
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		adapter.clearSelection();
		WebpageAdapter.clickable = false;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	private void deleteSelectedItems(Context context) {
		ArchiveHelper helper = new ArchiveHelper(null);
		WebpageCRUD crud = new WebpageCRUD(context);
		crud.openDB();
		for (int pos : adapter.getCurrentCheckedPosition()) {
			String title = ((Webpage) adapter.getItem(pos)).getTitle();
			crud.deleteWebpage(title);
			helper.deleteArchive(title);
		}
		crud.closeDB();
		parentAct.populateWebpages(context);
	}
}
