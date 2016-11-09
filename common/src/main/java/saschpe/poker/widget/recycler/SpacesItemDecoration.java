package saschpe.poker.widget.recycler;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * SpacesItemDecoration is a {@link RecyclerView.ItemDecoration} that can be used to add spacing
 * between items of a {@link LinearLayoutManager}. It supports both {@link #HORIZONTAL} and
 * {@link #VERTICAL} orientations.
 *
 * <pre>
 *     mSpacesItemDecoration = new SpacesItemDecoration(recyclerView.getContext(), 16,
 *             mLayoutManager.getOrientation());
 *     recyclerView.addItemDecoration(mDividerItemDecoration);
 * </pre>
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = OrientationHelper.VERTICAL;

    private int mSpace;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param space Item spacing in PX
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public SpacesItemDecoration(int space, int orientation) {
        this.mSpace = space;
        setOrientation(orientation);
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL) {
            outRect.left = mSpace;
            outRect.right = mSpace;
        } else {
            outRect.top = mSpace;
            outRect.bottom = mSpace;
        }
    }
}