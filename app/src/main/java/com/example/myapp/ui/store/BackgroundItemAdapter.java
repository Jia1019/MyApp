package com.example.myapp.ui.store;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.model.Background;

import java.util.ArrayList;

public class BackgroundItemAdapter extends RecyclerView.Adapter<BackgroundItemAdapter.ViewHolder> {
    private ArrayList<Background> backgroundList;
    final private OnListItemClickListener listener;

    public BackgroundItemAdapter(ArrayList<Background> backgroundList, OnListItemClickListener listener) {
        this.backgroundList = backgroundList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BackgroundItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.backgrounditem, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BackgroundItemAdapter.ViewHolder holder, int position) {

        Background background = backgroundList.get(position);
        holder.backgroundPicture.setImageResource(background.getUrl());
        if (!background.isOwned()) {
            holder.moneyNum.setText(String.valueOf(background.getCostNum()));
            holder.moneyIcon.setVisibility(View.VISIBLE);
            holder.applyBtn.setText("Purchase");
        } else {
            if (background.isApplied()) {
                holder.moneyNum.setText("APPLIED");
                holder.moneyIcon.setVisibility(View.INVISIBLE);
                holder.applyBtn.setText("Apply");
            } else {
                holder.moneyNum.setText("OWNED");
                holder.moneyIcon.setVisibility(View.INVISIBLE);
                holder.applyBtn.setText("Apply");
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isFront)
                {
                    holder.isFront = false;
                    if (!holder.isFront)
                    {
                        holder.applyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.update(position);
                                holder.displayNextView.onAnimationEnd(holder.cardView.getAnimation());
                            }
                        });


                        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    listener.cancelApply(position);
                                holder.displayNextView.onAnimationEnd(holder.cardView.getAnimation());
                            }
                        });


                    }
                }
                else
                {
                    holder.isFront = true;
                }
                holder.displayNextView.onAnimationEnd(holder.cardView.getAnimation());
            }
        });

    }

    @Override
    public int getItemCount() {
        return backgroundList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView backgroundPicture;
        public TextView moneyNum;
        public ImageView moneyIcon;
        public Button applyBtn;
        public Button cancelBtn;
        public boolean isFront;
        public CardView cardView;
        public ConstraintLayout front;
        public ConstraintLayout back;
        public final DisplayNextView displayNextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundPicture = itemView.findViewById(R.id.backgroundPicture);
            moneyNum = itemView.findViewById(R.id.moneyNum);
            moneyIcon = itemView.findViewById(R.id.moneyicon);
            applyBtn = itemView.findViewById(R.id.applyBtn);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
            cardView = (CardView) itemView.findViewById(R.id.choose_background_item);
            front= (ConstraintLayout) itemView.findViewById(R.id.turnfront);
            back= (ConstraintLayout) itemView.findViewById(R.id.turnback);
            isFront = true;
            displayNextView=new DisplayNextView();

            //cardView.setOnClickListener(new View.OnClickListener() {
              //  @Override
                //public void onClick(View v) {
                  //  displayNextView.onAnimationEnd(cardView.getAnimation());
                //}
            //});

        }

        @Override
        public void onClick(View v) {
            listener.onListItemClick(getAdapterPosition());

        }

        private final class DisplayNextView implements Animation.AnimationListener {

            public void onAnimationStart(Animation animation) {
            }

            // 动画结束
            public void onAnimationEnd(Animation animation) {

                cardView.post(() -> {
                    if (front.getVisibility() == View.VISIBLE) {
                        applyRotation(cardView, 90, 0, false);
                    } else {
                        applyRotation(cardView, -90, 0, false);
                    }
                });

                if (front.getVisibility() == View.VISIBLE) {
                    front.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);

                } else {
                    front.setVisibility(View.VISIBLE);
                    back.setVisibility(View.GONE);

                }
            }

            public class Rotate3dAnimation extends Animation {
                // 开始角度
                private final float mFromDegrees;
                // 结束角度
                private final float mToDegrees;
                // 中心点
                private final float mCenterX;
                private final float mCenterY;
                private final float mDepthZ;
                // 是否需要扭曲
                private final boolean mReverse;
                // 摄像头
                private Camera mCamera;

                public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX,
                                         float centerY, float depthZ, boolean reverse) {
                    mFromDegrees = fromDegrees;
                    mToDegrees = toDegrees;
                    mCenterX = centerX;
                    mCenterY = centerY;
                    mDepthZ = depthZ;
                    mReverse = reverse;
                }

                @Override
                public void initialize(int width, int height, int parentWidth,
                                       int parentHeight) {
                    super.initialize(width, height, parentWidth, parentHeight);
                    mCamera = new Camera();
                }

                // 生成Transformation
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    final float fromDegrees = mFromDegrees;
                    // 生成中间角度
                    float degrees = fromDegrees
                            + ((mToDegrees - fromDegrees) * interpolatedTime);

                    final float centerX = mCenterX;
                    final float centerY = mCenterY;
                    final Camera camera = mCamera;

                    final Matrix matrix = t.getMatrix();

                    camera.save();
                    if (mReverse) {
                        camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
                    } else {
                        camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
                    }
                    camera.rotateY(degrees);
                    // 取得变换后的矩阵
                    camera.getMatrix(matrix);
                    camera.restore();

                    matrix.preTranslate(-centerX, -centerY);
                    matrix.postTranslate(centerX, centerY);
                }
            }

            /**
             * 动画执行动作监听
             */


            private void applyRotation(View view, float start, float end, boolean reverse) {
                // 计算中心点
                float centerX = view.getWidth() / 2.0f;
                float centerY = view.getHeight() / 2.0f;
                Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
                        centerX, centerY, 300.0f, reverse);
                rotation.setDuration(300);
                rotation.setFillAfter(true);
                rotation.setInterpolator(new AccelerateInterpolator());
                if (reverse)
                    rotation.setAnimationListener(new DisplayNextView()); // 设置监听
                view.startAnimation(rotation);
            }

            public void onAnimationRepeat(Animation animation) {
            }
        }


    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);

        void update(int position);

        void cancelApply(int position);
    }






}
