package com.zsy.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.zsy.tool.dto.TopInfoDto;
import com.zsy.tool.task.TopTask;
import com.zsy.tool.util.ShellUtils;
import com.zsy.tool.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class TopActivity extends Activity {

	private LinkedBlockingQueue<List<TopInfoDto>> queue = new LinkedBlockingQueue<List<TopInfoDto>>();

	class CpuComparator implements Comparator<TopInfoDto> {

		@Override
		public int compare(TopInfoDto o1, TopInfoDto o2) {
			if (o1 == null || o2 == null) {
				return 0;
			}
			return o2.getCpu() - o1.getCpu();
		}
	};

	class RamComparator implements Comparator<TopInfoDto> {

		@Override
		public int compare(TopInfoDto o1, TopInfoDto o2) {
			if (o1 == null || o2 == null) {
				return 0;
			}
			return (int) (o2.getRss() - o1.getRss());
		}
	};

	private Comparator<TopInfoDto> cpuComparator = new CpuComparator();
	private Comparator<TopInfoDto> ramComparator = new RamComparator();

	private Comparator<TopInfoDto> comparator = ramComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);
		this.getActionBar().hide();
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				TopTask task = new TopTask(queue, comparator);
				while (true) {
					task.execute();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						Log.e("errorMsg", e.getMessage(), e);
					}
				}

			}
		});

		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						List<TopInfoDto> topInfos = queue.take();
						final List<TextView> textViews = new ArrayList<TextView>();
						TextView textView = new TextView(
								getApplicationContext());
						textView.setTextColor(android.graphics.Color.BLACK);
						textView.setText(TopInfoDto.getTitle());
						textView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								if (comparator == cpuComparator) {
									comparator = ramComparator;
									Toast.makeText(getApplicationContext(),
											"«–ªªµΩram≈≈–Ú", Toast.LENGTH_SHORT)
											.show();
								} else if (comparator == ramComparator) {
									comparator = cpuComparator;
									Toast.makeText(getApplicationContext(),
											"«–ªªµΩcpu≈≈–Ú", Toast.LENGTH_SHORT)
											.show();
								}
								
							}
						});
						textViews.add(textView);
						if (comparator != null) {
							Collections.sort(topInfos, comparator);
						}
						for (TopInfoDto dto : topInfos) {
							textViews.add(constructTextView(dto));
						}
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
								tableLayout.removeAllViews();
								for (TextView textView : textViews) {
									TableRow tableRow = new TableRow(
											getApplicationContext());
									tableRow.setLayoutParams(new LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT));
									LinearLayout linerLayout = new LinearLayout(
											getApplicationContext());
									textView.setLayoutParams(new LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT));
									linerLayout.addView(textView);
									tableRow.addView(linerLayout);
									tableLayout.addView(tableRow);
								}
							}
						});

					} catch (InterruptedException e) {
						Log.e("errorMsg", e.getMessage(), e);
					}
				}
			}

			private TextView constructTextView(TopInfoDto dto) {
				final TextView textView = new TextView(getApplicationContext());
				textView.setTextColor(android.graphics.Color.BLACK);
				StringBuffer sb = new StringBuffer();
				sb.append(dto.toString());

				textView.setText(sb.toString());
				final long pid = dto.getPid();
				final String pName = dto.getName();
				textView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						new AlertDialog.Builder(TopActivity.this)
								.setMessage("killΩ¯≥Ã" + pid + "(" + pName + ")")
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												ShellUtils.execCommand(
														"kill -9 " + pid, true,
														false);
												Toast.makeText(
														getApplicationContext(),
														"kill " + pid + "("
																+ pName + ")",
														Toast.LENGTH_SHORT)
														.show();
											}
										})
								.setNegativeButton("NO",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												Toast.makeText(
														getApplicationContext(),
														"not kill " + pid + "("
																+ pName + ")",
														Toast.LENGTH_SHORT)
														.show();

											}
										}).create().show();
					}
				});
				return textView;
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			return true;
		}
	};

}
