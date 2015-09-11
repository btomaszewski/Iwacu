/* Copyright 2013 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the use restrictions 
 * http://help.arcgis.com/en/sdk/10.0/usageRestrictions.htm.
 */

package gis.iwacu_new.rit.edu.main;

import gis.iwacu_new.rit.edu.main.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class GeometrySampleActivity extends FragmentActivity implements
		SampleListFragment.OnSampleNameSelectedListener {

	ProjectFragment projectFrag;

	BufferFragment bufferFrag;

	UnionDifferenceFragment uniondiffFrag;

	SpatialRelationshipsFragment spatialrelationFrag;

	MeasureFragment measureFrag;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geometry_samples);

		// Check whether the activity is using the layout version with
		// the fragment_container FrameLayout. If so, we must add the first
		// fragment
		if (findViewById(R.id.fragment_container) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create an instance of the SampleListFragment
			SampleListFragment firstFragment = new SampleListFragment();

			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
		
		
		//open specific tool if being called from learning content
		//see if there are incoming args
		Intent intent = getIntent();
		
		
		if (intent.hasExtra(getResources().getString((R.string.tool_argument_key_name)))) {
			
			Bundle incoming_data = intent.getBundleExtra(getResources().getString((R.string.tool_argument_key_name)));
		
			if (incoming_data != null) {
				
				String tool_name = (String) incoming_data.get(getResources().getString((R.string.tool_argument_tool_name)));
				
				String tool_data = (String) incoming_data.get(getResources().getString((R.string.tool_argument_tool_data)));
				
				
				String [] GIS_tools = getResources().getStringArray(R.array.sample_names);
				
				int postion_to_use = 0;
				
				for (int x = 0; x < GIS_tools.length; x++) {
	        		
	        		
	        		
	        		if (GIS_tools[x].equalsIgnoreCase(tool_name)) {
	        			
	        			//find the tool being called and pass arguments in accordingly
	        			switch (x) {
	        			case 0:
	        				postion_to_use = x;
	        				break;
	        			case 1:
	        				postion_to_use = x;
	        				break;
	        			case 2:
	        				postion_to_use = x;
	        				break;
	        			case 3:
	        				postion_to_use = x;
	        				break;
	        			case 4:
	        				postion_to_use = x;
	        				break;
	        			default:
	        				break;
	        				
	        			}
	        			
	        		}
				}
				
				
				//make the actual tools call
				this.onArticleSelected(postion_to_use);
				
				
			}
		
		}
	}

	public void onArticleSelected(int position) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (projectFrag != null && !projectFrag.isDetached()) {

			ft.detach(projectFrag);
			projectFrag = null;
		}

		if (bufferFrag != null && !bufferFrag.isDetached()) {

			ft.detach(bufferFrag);
			bufferFrag = null;
		}

		if (uniondiffFrag != null && !uniondiffFrag.isDetached()) {

			ft.detach(uniondiffFrag);
			uniondiffFrag = null;
		}

		if (spatialrelationFrag != null && !spatialrelationFrag.isDetached()) {

			ft.detach(spatialrelationFrag);
			spatialrelationFrag = null;
		}

		if (measureFrag != null && !measureFrag.isDetached()) {

			ft.detach(measureFrag);
			measureFrag = null;
		}

		switch (position) {
		case 0:

			if (projectFrag == null || projectFrag.getShownIndex() != position) {
				// Make new fragment to show this selection.
				projectFrag = ProjectFragment.newInstance(position);

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.
				// FragmentTransaction ft =
				// getSupportFragmentManager().beginTransaction();

				ft.replace(R.id.sample_fragment, projectFrag);

				ft.setTransition(FragmentTransaction.TRANSIT_NONE);
				ft.commit();
			}

			break;

		case 1:

			if (bufferFrag == null || bufferFrag.getShownIndex() != position) {
				// Make new fragment to show this selection.
				bufferFrag = BufferFragment.newInstance(position);

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.

				ft.add(R.id.sample_fragment, bufferFrag);

				ft.setTransition(FragmentTransaction.TRANSIT_NONE);
				ft.commit();
			}

			break;

		case 2:

			if (uniondiffFrag == null
					|| uniondiffFrag.getShownIndex() != position) {
				// Make new fragment to show this selection.
				uniondiffFrag = UnionDifferenceFragment.newInstance(position);

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.

				ft.add(R.id.sample_fragment, uniondiffFrag);

				ft.setTransition(FragmentTransaction.TRANSIT_NONE);
				ft.commit();
			}

			break;

		case 3:

			if (spatialrelationFrag == null
					|| spatialrelationFrag.getShownIndex() != position) {
				// Make new fragment to show this selection.
				spatialrelationFrag = SpatialRelationshipsFragment
						.newInstance(position);

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.

				ft.add(R.id.sample_fragment, spatialrelationFrag);

				ft.setTransition(FragmentTransaction.TRANSIT_NONE);
				ft.commit();
			}

			break;

		case 4:

			if (measureFrag == null || measureFrag.getShownIndex() != position) {
				// Make new fragment to show this selection.
				measureFrag = MeasureFragment.newInstance(position);

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.

				ft.add(R.id.sample_fragment, measureFrag);

				ft.setTransition(FragmentTransaction.TRANSIT_NONE);
				ft.commit();
			}

			break;

		default:
			break;

		}
	}
}