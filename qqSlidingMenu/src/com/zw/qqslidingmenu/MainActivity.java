package com.zw.qqslidingmenu;

import com.zw.qqslidingmenu.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	private SlidingMenu mLeftMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        mLeftMenu = (SlidingMenu) findViewById(R.id.id_menu);
    }
    
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
    	// TODO Auto-generated method stub
    	return super.onMenuOpened(featureId, menu);
    }
    
    public void toggleMenu(View view) {
    	mLeftMenu.toggle();
    }
}
