package edu.uaic.fii.wad.edec.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.model.Rule;
import edu.uaic.fii.wad.edec.listener.RuleOnClickListener;

public class GroupDetailsFragment extends Fragment {

    public static PageFragmentListener scanPageListener;

    private Spinner ruleType;
    private EditText ruleName;
    private Spinner ruleReason;
    private EditText groupName;
    private EditText groupDescription;
    private static Button addRule;
    private static Button saveGroup;
    private static Button saveGroup2;
    private static Button joinGroup;
    private static Button leaveGroup;
    private static Button editGroup;
    private static Button editGroup2;
    private static Button editRule;

    private static LinearLayout rulesLayout;
    private LinearLayout.LayoutParams rulesLayoutParams;
    private TextView ruleView;
    private TextView rulesMessage;
    private static int ruleIndex = 0;
    public static ImageView groupLogo;
    public static int RESULT_LOAD_IMAGE = 1;

    public GroupDetailsFragment(PageFragmentListener listener) {
        scanPageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.group_details_fragment, container, false);

        groupLogo = (ImageView) view.findViewById(R.id.add_group_logo);
        this.groupName = (EditText) view.findViewById(R.id.add_group_name);
        this.groupDescription = (EditText) view.findViewById(R.id.add_group_description);
        this.groupDescription.setSingleLine(false);

        TextView ruleHeader = (TextView) view.findViewById(R.id.add_group_new_rule);

        this.ruleType = (Spinner) view.findViewById(R.id.add_group_rule_type);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.rule_items, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        this.ruleType.setAdapter(adapter);

        this.ruleName = (EditText) view.findViewById(R.id.add_group_rule_name);

        this.ruleReason = (Spinner) view.findViewById(R.id.add_group_rule_reason);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.reason_items, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        this.ruleReason.setAdapter(adapter);

        rulesLayout = (LinearLayout) view.findViewById(R.id.add_group_existing_rules_list);

        rulesLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        rulesLayoutParams.setMargins((int) getResources().getDimension(R.dimen.margin),
                0,
                (int) getResources().getDimension(R.dimen.margin),
                (int) getResources().getDimension(R.dimen.space));

        rulesMessage = new TextView(getActivity());
        rulesMessage.setText("No rules added");
        rulesMessage.setHeight(50);
        rulesMessage.setTextColor(Color.BLACK);
        rulesMessage.setId(-1);
        rulesMessage.setTypeface(null, Typeface.ITALIC);
        rulesMessage.setGravity(Gravity.CENTER);

        rulesMessage.setLayoutParams(this.rulesLayoutParams);

        addNoRuleMessage();

        addRule = (Button) view.findViewById(R.id.add_group_save_rule);
        addRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRule();
            }
        });

        saveGroup = (Button) view.findViewById(R.id.add_group_save_group);
        saveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGroup();
            }
        });

        saveGroup2 = (Button) view.findViewById(R.id.add_group_save_group2);
        saveGroup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGroup();
            }
        });

        joinGroup = (Button) view.findViewById(R.id.add_group_join);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup();
            }
        });

        leaveGroup = (Button) view.findViewById(R.id.add_group_leave);
        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveGroup();
            }
        });

        editGroup = (Button) view.findViewById(R.id.add_group_edit_group);
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editGroup();
            }
        });

        editGroup2 = (Button) view.findViewById(R.id.add_group_edit_group2);
        editGroup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editGroup();
            }
        });

        groupLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        editRule = (Button) view.findViewById(R.id.add_group_edit_rule);

        if (MainActivity.groupState == 1) {
            setGroupInfo();
            saveGroup.setVisibility(View.GONE);
            editGroup.setVisibility(View.VISIBLE);

            if (GroupsFragment.thumb != null) {
                groupLogo.setImageBitmap(GroupsFragment.thumb);
            }
        } else if (MainActivity.groupState > 1) {
            setGroupInfo();
            groupName.setEnabled(false);
            groupDescription.setEnabled(false);
            addRule.setVisibility(View.GONE);
            saveGroup.setVisibility(View.GONE);
            ruleType.setEnabled(false);
            ruleName.setEnabled(false);
            ruleReason.setEnabled(false);
            ruleHeader.setText("Rule Details");

            if (MainActivity.currentGroup.getRules().size() > 0)  {
                Rule r = MainActivity.currentGroup.getRules().get(0);
                ruleType.setSelection(r.getType());
                ruleName.setText(r.getName());
                ruleReason.setSelection(r.getReason());
            }

            if (MainActivity.groupState == 2) {
                joinGroup.setVisibility(View.VISIBLE);
            } else {
                leaveGroup.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.groupState == 0) {
            setGroupInfo();
        }
    }

    public void backPressed() {
        scanPageListener.onSwitchToNextFragment(1, 0);
        ruleIndex = 0;
    }


    public void setGroupInfo() {
        this.groupName.setText(MainActivity.currentGroup.getName());
        this.groupDescription.setText(MainActivity.currentGroup.getDescription());

        if (MainActivity.currentGroup.getRules().size() > 0) {
            removeNoRuleMessage();
        }

        synchronized (MainActivity.currentGroup.getRules()) {
            for (Rule rule : MainActivity.currentGroup.getRules()) {
                displayRule(rule.getName(), rule.getType());
            }
        }
    }

    public void addRule() {
        int ruleTypeIndex = this.ruleType.getSelectedItemPosition();
        String ruleNameString = this.ruleName.getText().toString();
        int ruleReasonIndex = this.ruleReason.getSelectedItemPosition();

        Rule rule = new Rule(ruleTypeIndex, ruleNameString, ruleReasonIndex);

        if (MainActivity.currentGroup.getRules().contains(rule)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Replace existing ruleView?")
                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                        }
                    })
                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // ..
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (MainActivity.currentGroup.getRules().isEmpty()) {
                removeNoRuleMessage();
            }

            MainActivity.currentGroup.addRule(rule);
            displayRule(ruleNameString, ruleTypeIndex);
        }
    }

    public void displayRule(String ruleNameString, int ruleTypeIndex) {
        if (MainActivity.currentGroup.getRules().size() == 1) {
            this.removeNoRuleMessage();
        }
        ruleView = new TextView(getActivity());
        ruleView.setText(ruleNameString);
        ruleView.setHeight(50);
        ruleView.setTextColor(Color.BLACK);
        ruleView.setId(ruleIndex);

        if (ruleTypeIndex == 0) {
            ruleView.setBackgroundResource(R.drawable.company_rule_header);
        } else if (ruleTypeIndex == 1) {
            ruleView.setBackgroundResource(R.drawable.product_rule_header);
        } else {
            ruleView.setBackgroundResource(R.drawable.ingredient_rule_header);
        }
        ruleView.setGravity(Gravity.CENTER);

        ruleView.setLayoutParams(this.rulesLayoutParams);
        rulesLayout.addView(ruleView);

        this.ruleType.setSelection(0);
        this.ruleName.setText("");
        this.ruleReason.setSelection(0);

        if (MainActivity.groupState < 2) {
            ruleView.setOnClickListener(new RuleOnClickListener(ruleIndex, ruleView) {
                @Override
                public void onClick(View view) {
                    final CharSequence[] items = {
                            "Edit", "Delete"
                    };

                    final int ruleIndex = this.ruleIndex;
                    final TextView rule = this.ruleView;

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(rule.getText().toString());
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (item == 0) {
                                Rule r = MainActivity.currentGroup.getRules().get(ruleIndex);
                                ruleType.setSelection(r.getType());
                                ruleName.setText(r.getName());
                                ruleReason.setSelection(r.getReason());

                                displayButtons(1);

                                editRule.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (MainActivity.currentGroup.getRules().size() > 0) {
                                            int ruleTypeIndex = ruleType.getSelectedItemPosition();
                                            String ruleNameString = ruleName.getText().toString();
                                            int ruleReasonIndex = ruleReason.getSelectedItemPosition();

                                            Rule modifiedRule = MainActivity.currentGroup.getRules().get(ruleIndex);
                                            modifiedRule.setName(ruleNameString);
                                            modifiedRule.setType(ruleTypeIndex);
                                            modifiedRule.setReason(ruleReasonIndex);


                                            TextView rule = (TextView) rulesLayout.getChildAt(ruleIndex);
                                            rule.setText(ruleNameString);
                                            if (ruleTypeIndex == 0) {
                                                rule.setBackgroundResource(R.drawable.company_rule_header);
                                            } else if (ruleTypeIndex == 1) {
                                                rule.setBackgroundResource(R.drawable.product_rule_header);
                                            } else {
                                                rule.setBackgroundResource(R.drawable.ingredient_rule_header);
                                            }

                                            ruleType.setSelection(0);
                                            ruleName.setText("");
                                            ruleReason.setSelection(0);

                                            displayButtons(2);
                                        }
                                    }
                                });

                            } else {
                                clearRulesList();
                                MainActivity.currentGroup.getRules().remove(ruleIndex);
                                addNoRuleMessage();
                                setGroupInfo();

                                ruleType.setSelection(0);
                                ruleName.setText("");
                                ruleReason.setSelection(0);

                                displayButtons(2);
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        } else {
            ruleView.setOnClickListener(new RuleOnClickListener(ruleIndex, ruleView) {
                @Override
                public void onClick(View view) {
                    final int ruleIndex = this.ruleIndex;

                    Rule r = MainActivity.currentGroup.getRules().get(ruleIndex);
                    ruleType.setSelection(r.getType());
                    ruleName.setText(r.getName());
                    ruleReason.setSelection(r.getReason());
                }
            });
        }

        ruleIndex++;
    }

    public void saveGroup() {
       /*
        String groupName = this.groupName.getText().toString();
        String groupDescription = this.groupDescription.getText().toString();
        MainActivity.myGroups.add(new Group("" + MainActivity.myGroups.size(), groupName, MainActivity.currentGroupRules, groupDescription));

        //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.plus);
        Bitmap bitmap = ((BitmapDrawable) groupLogo.getDrawable()).getBitmap();
        MainActivity.newMyGroups.add((new GridItem(bitmap, groupName)));
        GroupsFragment.myGroupsCustomGridAdapter.notifyDataSetChanged();

        pageListener.onSwitchToNextFragment(1, 0);

        Log.i("working rules", "" + MainActivity.currentGroupRules.size());  */
    }

    public void joinGroup() {
       /* String groupName = this.groupName.getText().toString();
        String groupDescription = this.groupDescription.getText().toString();
        MainActivity.joinedGroups.add(new Group("" + MainActivity.joinedGroups.size(), groupName, MainActivity.currentGroupRules, groupDescription));

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.string.companies);
        MainActivity.newJoinedGroups.add((new GridItem(bitmap, groupName)));
        GroupsFragment.joinedGroupsCustomGridAdapter.notifyDataSetChanged();


        //MainActivity.friendsGroups.add(new Group(MainActivity.friendsGroups.size(), groupName, MainActivity.currentGroupRules, groupDescription));
        MainActivity.oldFriendsGroups.add((new GridItem(bitmap, groupName)));
        GroupsFragment.friendsGroupsCustomGridAdapter.notifyDataSetChanged();


        pageListener.onSwitchToNextFragment(1, 0);    */
    }

    public void leaveGroup() {
      /*  //mocking the shit out of it
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.string.companies);
        MainActivity.newJoinedGroups.remove(MainActivity.newJoinedGroups.size() - 1);
        GroupsFragment.joinedGroupsCustomGridAdapter.notifyDataSetChanged();


        //MainActivity.friendsGroups.add(new Group(MainActivity.friendsGroups.size(), groupName, MainActivity.currentGroupRules, groupDescription));
        MainActivity.oldFriendsGroups.remove(MainActivity.oldFriendsGroups.size() - 1);
        GroupsFragment.friendsGroupsCustomGridAdapter.notifyDataSetChanged();


        pageListener.onSwitchToNextFragment(1, 0);      */
    }

    public void editGroup() {
      /*  //mocking the shit out of this as well

        String groupName = this.groupName.getText().toString();
        String groupDescription = this.groupDescription.getText().toString();
        Bitmap bitmap = ((BitmapDrawable) groupLogo.getDrawable()).getBitmap();

        GroupsFragment.newGroupName = groupName;
        GroupsFragment.thumb = bitmap;

        //Collections.reverse(MainActivity.myGroups);
        MainActivity.myGroups.remove(MainActivity.myGroups.size() - 1);
        MainActivity.myGroups.add(new Group("" + MainActivity.myGroups.size(), groupName, MainActivity.currentGroupRules, groupDescription));
        //Collections.reverse(MainActivity.myGroups);

        GroupsFragment.myGroupsCustomGridAdapter.notifyDataSetChanged();
        pageListener.onSwitchToNextFragment(1, 0);   */

    }

    public void addNoRuleMessage() {
        rulesLayout.addView(rulesMessage);
    }

    public void removeNoRuleMessage() {
        rulesLayout.removeView(rulesMessage);
    }

    private static void clearRulesList() {
        rulesLayout.removeAllViewsInLayout();
        ruleIndex = 0;
    }

    private static void displayButtons(int value) {
        switch (value) {
            case 1: {
                if (MainActivity.groupState == 0) {
                    editRule.setVisibility(View.VISIBLE);
                    editGroup2.setVisibility(View.GONE);
                    addRule.setVisibility(View.GONE);
                    editGroup.setVisibility(View.GONE);
                    saveGroup.setVisibility(View.GONE);
                    saveGroup2.setVisibility(View.VISIBLE);
                } else {
                    editRule.setVisibility(View.VISIBLE);
                    editGroup2.setVisibility(View.VISIBLE);
                    addRule.setVisibility(View.GONE);
                    editGroup.setVisibility(View.GONE);
                    saveGroup.setVisibility(View.GONE);
                    saveGroup2.setVisibility(View.GONE);
                }
                break;
            }

            case 2: {
                if (MainActivity.groupState == 0) {
                    editRule.setVisibility(View.GONE);
                    editGroup2.setVisibility(View.GONE);
                    addRule.setVisibility(View.VISIBLE);
                    editGroup.setVisibility(View.GONE);
                    saveGroup.setVisibility(View.VISIBLE);
                    saveGroup2.setVisibility(View.GONE);
                } else {
                    editRule.setVisibility(View.GONE);
                    editGroup2.setVisibility(View.GONE);
                    addRule.setVisibility(View.VISIBLE);
                    editGroup.setVisibility(View.VISIBLE);
                    saveGroup.setVisibility(View.GONE);
                    saveGroup2.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

}