package idv.funnybrain.plurkchat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import idv.funnybrain.plurkchat.MainActivity;
import idv.funnybrain.plurkchat.R;

public class MyselfFragment extends Fragment {
    MyselfFragment newInstance() {
        MyselfFragment myselfFragment = new MyselfFragment();
        return myselfFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myself, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).doTest();

    }
}
