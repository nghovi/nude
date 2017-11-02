package nguyenhoangviet.vpcorp.calendar.services.todo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;
import com.daimajia.swipe.SwipeLayout;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.defines.ClConst;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.AbstractClFragment;
import nguyenhoangviet.vpcorp.calendar.services.todo.model.Todo;
import nguyenhoangviet.vpcorp.calendar.services.todo.view.TodoListAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.dialog.WfDialog;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * TodoListFragment
 *
 * @author VietNH
 */
public class TodoListFragment extends AbstractClFragment {

    private LinearLayout lnrUnfinished;
    private LinearLayout lnrFinished;
    private TodoListAdapter adapter;
    private List<Todo> todoList;
    private Button btnShowFinished;
    private WfDialog dlgDeleteConfirm;
    private LayoutInflater inflater;
    private SwipeLayout swipeLayout;
    private Date today;
    private Todo selectedTodo;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
        }
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        today = Calendar.getInstance().getTime();
        initHeader(null, getString(R.string.todo_title), R.drawable.cl_action_add);
        getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        scrollView = (ScrollView) getView().findViewById(R.id.scr_todo);
        lnrUnfinished = (LinearLayout) getView().findViewById(R.id.lnr_unfinished_todo_containter);
        lnrFinished = (LinearLayout) getView().findViewById(R.id.lnr_finished_todo_containter);
        btnShowFinished = (Button) getView().findViewById(R.id.btn_fragment_todo_list_show_finished);
        btnShowFinished.setOnClickListener(this);
        inflater = LayoutInflater.from(activity);
    }

    @Override
    protected void initData() {
        loadTodoList(true);
    }

    private void loadTodoList(boolean showLoading) {
        JSONObject jsonObject = new JSONObject();
        // try{
        // jsonObject.put("targetDateString", "2017/02/22");
        // }catch(JSONException e){
        // e.printStackTrace();
        // }
        requestLoad(ClConst.API_TODO_LIST, jsonObject, showLoading);

    }

    protected void successLoad(JSONObject response, String url) {
        try {
            todoList = LoganSquare.parseList(response.optString("todoList"), Todo.class);
            if (CCCollectionUtil.isEmpty(todoList)) {
                getView().findViewById(R.id.txt_todo_empty).setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            } else {
                getView().findViewById(R.id.txt_todo_empty).setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                buildTodos();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildTodos() {
        lnrFinished.removeAllViews();
        lnrUnfinished.removeAllViews();
        boolean hasFinishedTodo = false;
        for (Todo todo : todoList) {
            buildTodoItem(todo);
            if (todo.isFinish) {
                hasFinishedTodo = true;
            }
        }
        if (hasFinishedTodo) {
            btnShowFinished.setVisibility(View.VISIBLE);
        } else {
            btnShowFinished.setVisibility(View.GONE);
        }
    }

    private void buildTodoItem(final Todo todo) {
        if (todo.isFinish) {
            boolean atEnd = true;
            // buildFinishedTodoItem(todo, atEnd);
            final SwipeLayout cell = (SwipeLayout) inflater.inflate(R.layout.item_todo_finished, null);
            cell.getSurfaceView().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    gotoTodoDetail(todo);
                }
            });
            // set show mode
            cell.setShowMode(SwipeLayout.ShowMode.LayDown);

            // add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
            cell.addDrag(SwipeLayout.DragEdge.Left, cell.findViewById(R.id.bottom_wrapper));

            cell.addSwipeListener(new SwipeLayout.SwipeListener() {

                @Override
                public void onClose(SwipeLayout layout) {
                    // when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    // you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    // when the BottomView totally show.
                    swipeLayout = layout;
                    showDeleteDialog(todo);
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    // when user's hand released.
                }
            });

            TextView txtDate = (TextView) cell.findViewById(R.id.txt_item_todo_date);
            TextView txtTitle = (TextView) cell.findViewById(R.id.txt_item_todo_title);
            RadioButton radioButton = (RadioButton) cell.findViewById(R.id.item_todo_finished_radio);
            radioButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finishTodo(todo, false);
                }
            });
            // radioButton.setChecked(true);

            // Button btnDelete = (Button)cell.findViewById(R.id.btn_id_delete);
            // btnDelete.setOnClickListener(new View.OnClickListener() {
            //
            // @Override
            // public void onClick(View v){
            // showDeleteDialog(todo);
            // }
            // });
            if (todo.limitDate == null) {
                txtDate.setText(getString(R.string.no_deadline));
            } else {
                if (CCDateUtil.compareDate(today, todo.limitDate, false) >= 0) {
                    // txtDate.setTextColor(Color.RED);
                }
                if (CCDateUtil.compareDate(today, todo.limitDate, false) == 0) {
                    txtDate.setText(getString(R.string.chiase_common_today));
                } else {
                    txtDate.setText(CCFormatUtil.formatDateCustom("M/d",todo.limitDate));
                }
            }
            txtTitle.setText(todo.name);
            cell.setTag(lnrFinished.getChildCount());
            lnrFinished.addView(cell);
        } else {
            final SwipeLayout cell = (SwipeLayout) inflater.inflate(R.layout.item_todo_unfinished, null);
            cell.getSurfaceView().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    gotoTodoDetail(todo);
                }
            });
            // set show mode
            cell.setShowMode(SwipeLayout.ShowMode.LayDown);

            // add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
            cell.addDrag(SwipeLayout.DragEdge.Left, cell.findViewById(R.id.bottom_wrapper));

            cell.addSwipeListener(new SwipeLayout.SwipeListener() {

                @Override
                public void onClose(SwipeLayout layout) {
                    // when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    // you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    // when the BottomView totally show.
                    swipeLayout = layout;
                    showDeleteDialog(todo);
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    // when user's hand released.
                }
            });

            TextView txtDate = (TextView) cell.findViewById(R.id.txt_item_todo_date);
            TextView txtTitle = (TextView) cell.findViewById(R.id.txt_item_todo_title);
            RadioButton radioButton = (RadioButton) cell.findViewById(R.id.radio);
            radioButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finishTodo(todo, true);
                }
            });
            //
            // Button btnDelete = (Button)cell.findViewById(R.id.btn_id_delete);
            // btnDelete.setOnClickListener(new View.OnClickListener() {
            //
            // @Override
            // public void onClick(View v){
            // showDeleteDialog(todo);
            // }
            // });
            if (todo.limitDate == null) {
                txtDate.setText(getString(R.string.no_deadline));
            } else {
                if (CCDateUtil.compareDate(today, todo.limitDate, false) >= 0) {
                    txtDate.setTextColor(Color.RED);
                }
                if (CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, today).equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, todo.limitDate))) {
                    txtDate.setText(getString(R.string.chiase_common_today));
                } else {
                    txtDate.setText(CCFormatUtil.formatDateCustom("M/d", todo.limitDate));
                }
            }
            txtTitle.setText(todo.name);
            cell.setTag(lnrUnfinished.getChildCount());
            lnrUnfinished.addView(cell);
        }
    }

    private void finishTodo(Todo todo, boolean isFinish) {
        this.selectedTodo = todo;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", todo.key);
            jsonObject.put("name", todo.name);
            jsonObject.put("note", todo.note);
            jsonObject.put("isFinish", isFinish);
            jsonObject.put("limitDate", todo.limitDate == null ? null : CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, todo.limitDate));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ClConst.API_TODO_UPDATE, jsonObject, false);
    }

    private void showDeleteDialog(final Todo todo) {
        if (dlgDeleteConfirm == null || !dlgDeleteConfirm.isShowing()) {
            dlgDeleteConfirm = new WfDialog(activity);
            dlgDeleteConfirm.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (swipeLayout != null) {
                        swipeLayout.close(true, true);
                    }
                }
            });
            dlgDeleteConfirm.setDialogTitleButton(getString(R.string.sure_to_delete), getString(R.string.chiase_common_ok), getString(R.string.wf_cancel), new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    callDeleteApi(todo);
                }
            });
            dlgDeleteConfirm.show();
        }

    }

    private void callDeleteApi(Todo todo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", todo.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ClConst.API_TODO_DELETE, jsonObject, true);
    }

    private void gotoTodoDetail(Todo todo) {
        TodoDetailFragment fragment = new TodoDetailFragment();
        fragment.setTodo(todo);
        fragment.setEditable(true);
        gotoFragment(fragment);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_todo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment_todo_list_show_finished:
                onClickShowFinishedTodoBtn();
                break;
            case R.id.img_id_header_right_icon:
                gotoTodoDetail(null);
                break;
            default:
                break;
        }
    }

    private void onClickShowFinishedTodoBtn() {
        int visibility = lnrFinished.getVisibility();
        if (visibility == View.VISIBLE) {
            btnShowFinished.setText(getString(R.string.show_finished_task));
            lnrFinished.setVisibility(View.GONE);
        } else {
            scrollView.requestChildFocus(btnShowFinished, btnShowFinished);
            btnShowFinished.setText(getString(R.string.hide_unfinished_task));
            lnrFinished.setVisibility(View.VISIBLE);
            scrollView.post(new Runnable() {

                @Override
                public void run() {
                    // scrollView.fullScroll(View.FOCUS_DOWN);
                    scrollToView(scrollView, btnShowFinished, -16);
                }
            });
        }
    }

    public static void scrollToView(final ScrollView scrollViewParent, final View view, int offSetDp) {
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        scrollViewParent.smoothScrollTo(0, childOffset.y + WelfareUtil.dpToPx(offSetDp));
    }

    public static void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        if (ClConst.API_TODO_DELETE.equals(url)) {
            dlgDeleteConfirm.dismiss();
        }
        loadTodoList(false);
        // View cell = lnrUnfinished.findViewWithTag(selectedTodo.key);
        // lnrUnfinished.removeView(cell);
        // buildFinishedTodoItem(selectedTodo, false);
    }

}
