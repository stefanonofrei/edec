package edu.uaic.fii.wad.edec.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.*;
import android.widget.*;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.model.Rule;
import edu.uaic.fii.wad.edec.listener.RuleOnClickListener;
import edu.uaic.fii.wad.edec.service.group.EditGroup;
import edu.uaic.fii.wad.edec.service.group.SaveGroup;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

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
    private static Button editGroup;
    private static Button editGroup2;
    private static Button editRule;

    private static LinearLayout rulesLayout;
    private LinearLayout.LayoutParams rulesLayoutParams;
    private TextView rulesMessage;
    private static int ruleIndex = 0;
    public static ImageView groupLogo;
    public static int RESULT_LOAD_IMAGE = 1;
    public static String currentItemId = "";

    private int ruleTypeIndex;
    private String ruleNameString;
    private int ruleReasonIndex;

    public GroupDetailsFragment(PageFragmentListener listener) {
        scanPageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.group_details_fragment, container, false);

        ruleIndex = 0;

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

        this.ruleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter adapter;
                if (i == 0) {
                    adapter = ArrayAdapter.createFromResource(getActivity(), R.array.companies_reasons, R.layout.spinner_item);
                } else if (i == 1) {
                    adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ingredients_reasons, R.layout.spinner_item);
                } else {
                    adapter = ArrayAdapter.createFromResource(getActivity(), R.array.products_reasons, R.layout.spinner_item);
                }

                if (adapter != null) {
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    ruleReason.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // ...
            }
        });



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
                ruleTypeIndex = ruleType.getSelectedItemPosition();
                ruleNameString = ruleName.getText().toString();
                ruleReasonIndex = ruleReason.getSelectedItemPosition();

                String URL = URLs.baseURL;

                switch (ruleTypeIndex) {
                    case 0: {
                        URL += "/companies/" + ruleNameString + "/search.json";
                        break;
                    }
                    case 1: {
                        URL += "/ingredients/" + ruleNameString + "/search.json";
                        break;
                    }
                    case 2: {
                        URL += "/products/" + ruleNameString + "/search.json";
                        break;
                    }
                }

                new SearchItem(URL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

        Button joinGroup = (Button) view.findViewById(R.id.add_group_join);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup();
            }
        });

        Button leaveGroup = (Button) view.findViewById(R.id.add_group_leave);
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
        ruleIndex = 0;
        if (MainActivity.groupState == 0) {
            setGroupInfo();
        }
    }

    public void backPressed() {
        scanPageListener.onSwitchToNextFragment(1, 0);
        ruleIndex = 0;
    }


    public void setGroupInfo() {
        ruleIndex = 0;
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
        Rule rule;

        if (!currentItemId.equals("")) {
            rule = new Rule(ruleTypeIndex, ruleNameString, ruleReasonIndex, currentItemId);
            //Toast.makeText(getActivity().getApplicationContext(), currentItemId, Toast.LENGTH_LONG).show();

            if (MainActivity.currentGroup.getRules().contains(rule)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Replace existing ruleView?")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO replace existing rule
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
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Item does not exist!", Toast.LENGTH_LONG).show();
        }
    }

    public void displayRule(String ruleNameString, int ruleTypeIndex) {
        if (MainActivity.currentGroup.getRules().size() == 1) {
            this.removeNoRuleMessage();
        }
        TextView ruleView = new TextView(getActivity());
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
                                ruleType.setSelection(MainActivity.currentGroup.getRule(ruleIndex).getType());
                                ruleType.setEnabled(false);
                                ruleName.setText(MainActivity.currentGroup.getRule(ruleIndex).getName());
                                ruleName.setEnabled(false);
                                ruleReason.setSelection(MainActivity.currentGroup.getRule(ruleIndex).getReason());

                                displayButtons(1);

                                editRule.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (MainActivity.currentGroup.getRules().size() > 0) {
                                            int ruleTypeIndex = ruleType.getSelectedItemPosition();
                                            String ruleNameString = ruleName.getText().toString();
                                            int ruleReasonIndex = ruleReason.getSelectedItemPosition();

                                            MainActivity.currentGroup.getRule(ruleIndex).setName(ruleNameString);
                                            MainActivity.currentGroup.getRule(ruleIndex).setType(ruleTypeIndex);
                                            MainActivity.currentGroup.getRule(ruleIndex).setReason(ruleReasonIndex);

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
                                            ruleType.setEnabled(true);
                                            ruleName.setText("");
                                            ruleName.setEnabled(true);
                                            ruleReason.setSelection(0);

                                            displayButtons(2);
                                        }
                                    }
                                });

                            } else {
                                clearRulesList();
                                MainActivity.currentGroup.getRules().remove(ruleIndex);
                                addNoRuleMessage();
                                MainActivity.currentGroup.setName(groupName.getText().toString());
                                MainActivity.currentGroup.setDescription(groupDescription.getText().toString());
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
        MainActivity.currentGroup.setName(this.groupName.getText().toString());
        MainActivity.currentGroup.setDescription(this.groupDescription.getText().toString());

        groupLogo.buildDrawingCache();
        Bitmap bitmap = groupLogo.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] image = stream.toByteArray();
        String imageBase64 = Base64.encodeToString(image, 0);
        MainActivity.currentGroup.setLogo(imageBase64);

        new SaveGroup().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        GroupsFragment.pageListener.onSwitchToNextFragment(1, 0);
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
        MainActivity.currentGroup.setName(this.groupName.getText().toString());
        MainActivity.currentGroup.setDescription(this.groupDescription.getText().toString());

        groupLogo.buildDrawingCache();
        Bitmap bitmap = groupLogo.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] image = stream.toByteArray();
        String imageBase64 = Base64.encodeToString(image, 0);
        MainActivity.currentGroup.setLogo(imageBase64);

        new EditGroup().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        GroupsFragment.pageListener.onSwitchToNextFragment(1, 0);

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

    private class SearchItem extends AsyncTask<Void, Void, Void> {

        private String URL;

        public SearchItem(String URL) {
            this.URL = URL;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ServiceHandler serviceHandler = new ServiceHandler();
            String jsonStr = serviceHandler.makeServiceCall(URL, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONArray items = new JSONArray(jsonStr);

                    if (items.length() > 0) {
                        JSONObject searchResult = items.getJSONObject(0);
                        currentItemId = searchResult.getString("id");

                        jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + currentItemId + ".json", ServiceHandler.GET);

                        if (jsonStr != null) {
                            JSONObject item = new JSONObject(jsonStr);
                            ruleNameString = item.getString("name");
                        }
                    }  else {
                        currentItemId = "";
                    }
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            addRule();
        }
    }
}