package edu.uaic.fii.wad.edec.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.model.Rule;
import edu.uaic.fii.wad.edec.listener.RuleOnClickListener;
import edu.uaic.fii.wad.edec.service.group.EditGroup;
import edu.uaic.fii.wad.edec.service.group.GroupMigration;
import edu.uaic.fii.wad.edec.service.group.SaveGroup;
import edu.uaic.fii.wad.edec.service.util.Token;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupDetailsFragment extends Fragment {

    public static PageFragmentListener pageListener;

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
    public static int parent;
    public static boolean fromSearch;
    private boolean viewDetails;
    private static View view;

    public GroupDetailsFragment(PageFragmentListener listener) {
        pageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.group_details_fragment, container, false);

        ruleIndex = 0;
        viewDetails = false;

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

        this.ruleName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    ruleTypeIndex = ruleType.getSelectedItemPosition();
                    ruleNameString = ruleName.getText().toString();

                    String URL = URLs.baseURL;

                    switch (ruleTypeIndex) {
                        case 0: {
                            URL += "/companies/" + ruleNameString + "/search.json";
                            break;
                        }
                        case 1: {
                            URL += "/products/" + ruleNameString + "/search.json";
                            break;
                        }
                        case 2: {
                            URL += "/ingredients/" + ruleNameString + "/search.json";
                            break;
                        }
                    }

                    new SearchItem(URL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                return false;
            }
        });
        this.ruleReason = (Spinner) view.findViewById(R.id.add_group_rule_reason);

        setReasonsAdapter(0);

        this.ruleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!viewDetails) {
                    currentItemId = "";
                    setReasonsAdapter(i);
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
                ruleType.setSelection(r.getType(), false);
                ruleName.setText(r.getName());
                ruleReason.setSelection(r.getReason(), false);
            }

            if (MainActivity.groupState == 2) {
                joinGroup.setVisibility(View.VISIBLE);
            } else {
                leaveGroup.setVisibility(View.VISIBLE);
            }
        } else {
            parent = 1;
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

        view.post(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.loading.isShowing()) {
                    MainActivity.loading.dismiss();
                }
            }
        });
    }

    public void backPressed() {
        if (fromSearch) {
            pageListener.onSwitchToNextFragment(1, 1);
        } else {
            pageListener.onSwitchToNextFragment(parent, 0);
        }
        ruleIndex = 0;
        fromSearch = false;
    }

    private void setReasonsAdapter(int i) {
        ArrayAdapter adapter;
        if (i == 0) {
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.companies_reasons, R.layout.spinner_item);
        } else if (i == 1) {
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.products_reasons, R.layout.spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ingredients_reasons, R.layout.spinner_item);
        }

        if (adapter != null) {
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            ruleReason.setAdapter(adapter);
        }
    }

    public void setGroupInfo() {
        ruleIndex = 0;
        this.groupName.setText(MainActivity.currentGroup.getName());
        this.groupDescription.setText(MainActivity.currentGroup.getDescription());

        if (MainActivity.groupState != 0) {
            byte[] decodedString = Base64.decode(MainActivity.currentGroup.getLogo(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            groupLogo.setImageBitmap(decodedByte);
        }

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
        ruleReasonIndex = ruleReason.getSelectedItemPosition();

        if (!currentItemId.equals("")) {
            rule = new Rule(ruleTypeIndex, ruleNameString, ruleReasonIndex, currentItemId);

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

        this.ruleType.setSelection(0, false);
        this.ruleName.setText("");
        this.ruleReason.setSelection(0, false);

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
                                viewDetails = true;
                                ruleType.setSelection(MainActivity.currentGroup.getRule(ruleIndex).getType(), false);
                                ruleType.setEnabled(false);
                                ruleName.setText(MainActivity.currentGroup.getRule(ruleIndex).getName());
                                ruleName.setEnabled(false);
                                setReasonsAdapter(MainActivity.currentGroup.getRule(ruleIndex).getType());
                                ruleReason.setSelection(MainActivity.currentGroup.getRule(ruleIndex).getReason(), false);

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

                                            ruleType.setSelection(0, false);
                                            ruleType.setEnabled(true);
                                            ruleName.setText("");
                                            ruleName.setEnabled(true);
                                            ruleReason.setSelection(0, false);

                                            viewDetails = false;
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

                                ruleType.setSelection(0, false);
                                ruleType.setEnabled(true);
                                ruleName.setText("");
                                ruleName.setEnabled(true);
                                ruleReason.setSelection(0, false);

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
                    viewDetails = true;
                    Rule r = MainActivity.currentGroup.getRules().get(ruleIndex);
                    ruleType.setSelection(r.getType(), false);
                    ruleName.setText(r.getName());
                    setReasonsAdapter(MainActivity.currentGroup.getRule(ruleIndex).getType());
                    ruleReason.setSelection(r.getReason(), false);
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

        fromSearch = false;
        GroupsFragment.pageListener.onSwitchToNextFragment(1, 0);
    }

    public void joinGroup() {
        new GroupMigration(MainActivity.currentGroup.getId(), "/join").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        fromSearch = false;
        GroupsFragment.pageListener.onSwitchToNextFragment(parent, 0);
    }

    public void leaveGroup() {
        new GroupMigration(MainActivity.currentGroup.getId(), "/leave").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        fromSearch = false;
        GroupsFragment.pageListener.onSwitchToNextFragment(parent, 0);
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

        fromSearch = false;
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
        private ArrayList<BasicNameValuePair> searchResults = new ArrayList<BasicNameValuePair>();

        public SearchItem(String URL) {
            this.URL = URL;
            this.searchResults = new ArrayList<BasicNameValuePair>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse;
            HttpGet httpGet = new HttpGet(URL);

            httpGet.setHeader("Authorization", Token.CURRENT);

            String jsonStr = null;

            try {
                httpResponse = httpClient.execute(httpGet);
                if (httpResponse != null) {
                    httpEntity = httpResponse.getEntity();
                }

                jsonStr = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            if (jsonStr != null) {
                try {
                    JSONArray items = new JSONArray(jsonStr);

                    for (int i = 0; i < items.length(); i++) {

                        JSONObject searchResult = items.getJSONObject(i);
                        String id = searchResult.getString("id");

                        httpGet = new HttpGet(URLs.baseURL + id + ".json");

                        httpGet.setHeader("Authorization", Token.CURRENT);

                        try {
                            httpResponse = httpClient.execute(httpGet);
                            if (httpResponse != null) {
                                httpEntity = httpResponse.getEntity();
                            }

                            jsonStr = EntityUtils.toString(httpEntity);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }

                        if (jsonStr != null) {
                            JSONObject item = new JSONObject(jsonStr);
                            String ruleName = item.getString("name");

                            searchResults.add(new BasicNameValuePair(id, ruleName));
                        }
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
            MainActivity.loading.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (searchResults.size() > 1) {
                CharSequence[] items = new CharSequence[searchResults.size()];

                for (int i = 0; i < searchResults.size(); i++) {
                    items[i] = searchResults.get(i).getValue();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please select an item");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentItemId = searchResults.get(i).getName();
                        ruleNameString = searchResults.get(i).getValue();
                        ruleName.setText(ruleNameString);
                    }
                });
                builder.show();
            } else if (searchResults.size() == 1) {
                currentItemId = searchResults.get(0).getName();
                ruleNameString = searchResults.get(0).getValue();
                ruleName.setText(ruleNameString);
            } else {
                currentItemId = "";
            }

            MainActivity.loading.dismiss();
        }
    }
}