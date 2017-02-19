package eu.tesseractsoft.mintimedialog.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.tesseractsoft.mintimedialog.MinTimeDialog;

public class TestingActivity extends AppCompatActivity {

    @BindView(R.id.rvMain)
    RecyclerView rvMain;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TestingAdapterItemModel> mTestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        ButterKnife.bind(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvMain.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rvMain.setLayoutManager(mLayoutManager);

        mTestList = new ArrayList<>();

        addTests();

        // specify an adapter (see also next example)
        mAdapter = new TestingAdapter(mTestList.toArray(new TestingAdapterItemModel[mTestList.size()]));
        rvMain.setAdapter(mAdapter);

    }

    private void addTests() {
        mTestList.add(test0MinTimeInstantDismiss());
        mTestList.add(test0MinTimeWaitDismiss());
        mTestList.add(testDismissBeforeMinTime());
        mTestList.add(testDismissAfterMinTime());
        mTestList.add(testAutoDismiss());
        mTestList.add(testAutoDismiss0MinTime());
        mTestList.add(testDismissBeforeAutoDismiss());
        mTestList.add(testDismissForcedBeforeAutoDismiss());
        mTestList.add(testDismissForcedBeforeMinTime());
        mTestList.add(testDismissForcedAfterMinTime());
        mTestList.add(testSilentDismiss());
        mTestList.add(testSilentDismissForced());
        mTestList.add(testMinTimeReachedListener());
        mTestList.add(test0MinTimeMinTimeReachedListener());
    }

    private TestingAdapterItemModel test0MinTimeInstantDismiss() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "test0MinTimeInstantDismiss";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 0);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime < 100) {
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel test0MinTimeWaitDismiss() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "test0MinTimeWaitDismiss";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 0);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime > 800) {
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 1000);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testDismissBeforeMinTime() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testDismissBeforeMinTime";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 2000);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime > 1300) {
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });

                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 1000);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testDismissAfterMinTime() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testDismissAfterMinTime";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 1000);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime > 1300) {
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 2000);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testAutoDismiss() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testAutoDismiss";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 1000);
                        dialog.setAutoDismissAfterMinShownTime(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime < 1300) { // less than 2000
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();// dismiss after 2 sec - to fail test
                            }
                        }, 2000);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testAutoDismiss0MinTime() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testAutoDismiss0MinTime";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 0);
                        dialog.setAutoDismissAfterMinShownTime(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                long endTime = System.currentTimeMillis();
                                if (endTime - startTime < 500) { // less than 1000
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();// dismiss after 1 sec - to fail test
                            }
                        }, 1000);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testDismissBeforeAutoDismiss() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testDismissBeforeAutoDismiss";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 1000);
                        dialog.setAutoDismissAfterMinShownTime(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime > 900) {
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 500);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testDismissForcedBeforeAutoDismiss() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testDismissForcedBeforeAutoDismiss";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 1000);
                        dialog.setAutoDismissAfterMinShownTime(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime < 700) { // less than 1000
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismissForced();
                            }
                        }, 500);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testDismissForcedBeforeMinTime() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testDismissForcedBeforeMinTime";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 1000);
                        dialog.setAutoDismissAfterMinShownTime(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime < 800) { // less than 1000 which is min time
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismissForced();
                            }
                        }, 500);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testDismissForcedAfterMinTime() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testDismissForcedAfterMinTime";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final long startTime = System.currentTimeMillis();

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 1000);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (System.currentTimeMillis() - startTime > 1200) {
                                    updateStatus(getString(R.string.txt_pass));
                                } else {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismissForced();
                            }
                        }, 1500);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testSilentDismiss() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testSilentDismiss";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 1000);
                        dialog.setSilentDismiss(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateStatus(getString(R.string.txt_fail));
                                    }
                                }, 500);
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateStatus(getString(R.string.txt_pass));
                                dialog.dismiss();
                            }
                        }, 100);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testSilentDismissForced() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testSilentDismissForced";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 1000);
                        dialog.setSilentDismiss(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateStatus(getString(R.string.txt_fail));
                                    }
                                }, 500);
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateStatus(getString(R.string.txt_pass));
                                dialog.dismissForced();
                            }
                        }, 100);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel testMinTimeReachedListener() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "testMinTimeReachedListener";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final boolean[] ifPassed = {false};
                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 500);
                        dialog.setMinTimeReachedListener(new MinTimeDialog.MinTimeReachedListener() {
                            @Override
                            public void onMinTimeReached() {
                                updateStatus(getString(R.string.txt_pass));
                                ifPassed[0] = true;
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!ifPassed[0]) {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                                dialog.dismiss();
                            }
                        }, 1000);
                    }
                };
            }
        };
    }

    private TestingAdapterItemModel test0MinTimeMinTimeReachedListener() {
        return new TestingAdapterItemModel() {
            @Override
            public String getDescription() {
                return "test0MinTimeMinTimeReachedListener";
            }

            @Override
            public View.OnClickListener getOnClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final boolean[] ifPassed = {false};
                        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(TestingActivity.this, "Simple processing", 0);
                        dialog.setMinTimeReachedListener(new MinTimeDialog.MinTimeReachedListener() {
                            @Override
                            public void onMinTimeReached() {
                                updateStatus(getString(R.string.txt_pass));
                                ifPassed[0] = true;
                            }
                        });
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!ifPassed[0]) {
                                    updateStatus(getString(R.string.txt_fail));
                                }
                                dialog.dismiss();
                            }
                        }, 800);
                    }
                };
            }
        };
    }
}
