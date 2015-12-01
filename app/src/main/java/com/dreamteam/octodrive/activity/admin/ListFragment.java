package com.dreamteam.octodrive.activity.admin;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.interfaces.Listable;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.model.User;
import com.dreamteam.octodrive.utilities.LoadingView;
import com.parse.ParseException;

import java.util.List;


public class ListFragment extends Fragment {

    private Class mDataClass;
    private List<? extends Listable> mValues;

    private boolean _canAddNew;

    private LoadingView mLoadingView;
    private AllUsersTask mUserTask;

    public ListFragment(Class dataClass) {
        mDataClass = dataClass;
    }

    public static ListFragment newInstance(Class dataClass, boolean canAddNew) {
        ListFragment fragment = new ListFragment(dataClass);
        fragment._canAddNew = canAddNew;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (_canAddNew) {
            //TODO: Add FAB somehow....
//            RecyclerView recyclerView = (RecyclerView)view;
//            FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.button_add_new);
//            fab.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && mValues == null) {
            mLoadingView = new LoadingView(this.getActivity(), getString(R.string.dialog_loading));
            mLoadingView.showProgress(true);
            mUserTask = new AllUsersTask();
            mUserTask.execute((Void) null);
        }
    }

    private void reloadView() {
        View view = getView();
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ListItemViewAdapter(context, mValues));
        }
    }

    private class AllUsersTask extends AsyncTask<Void, Void, List<? extends Listable>> {
        AllUsersTask() {}

        @Override
        protected List<? extends Listable> doInBackground(Void... params) {
            try {
                if (mDataClass == User.class) {
                    return User.getAllUsers();
                }
                else if (mDataClass == Question.class) {
                    return Question.getAllQuestions();
                }
            }
            catch (ParseException e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<? extends Listable> objects) {
            mValues = objects;
            reloadView();
            mLoadingView.showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mLoadingView.showProgress(false);
        }
    }
}
