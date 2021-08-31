package com.xuexiang.xuidemo.fragment.components.refresh.broccoli;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.NewsListAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019/4/7 上午10:58
 */
@Page(name = "普通占位控件")
public class CommonPlaceholderFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private NewsListAdapter mNewsListAdapter;
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_broccoli_place_holder;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("截图") {
            @SingleClick
            @Override
            public void performAction(View view) {
                Utils.showCaptureBitmap(getContext(), Utils.getRecyclerViewScreenSpot(recyclerView));
            }
        });
        return titleBar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(recyclerView, DensityUtils.dp2px(5), ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_background));

        recyclerView.setAdapter(mNewsListAdapter = new NewsListAdapter(false));
    }

    @Override
    protected void initListeners() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    mNewsListAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
                    refreshLayout.finishLoadMore();
                }, 1000);
            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    mNewsListAdapter.refresh(DemoDataProvider.getDemoNewInfos());
                    refreshLayout.finishRefresh();
                }, 3000);
            }
        });

        mNewsListAdapter.setOnItemClickListener((itemView, item, position) -> Utils.goWeb(getContext(), item.getDetailUrl()));

        //设置刷新加载时禁止所有列表操作
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        mNewsListAdapter.recycle();
        super.onDestroyView();
    }


}
