package com.smart.library.support.constraint;

import android.content.*;
import java.util.*;
import android.graphics.*;
import android.view.*;
import android.content.res.*;
import android.util.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.solver.widgets.*;
import java.lang.reflect.*;

public abstract class ConstraintHelper extends View
{
    protected int[] mIds;
    protected int mCount;
    protected Context myContext;
    protected Helper mHelperWidget;
    protected boolean mUseViewMeasure;
    protected String mReferenceIds;
    private View[] mViews;
    private HashMap<Integer, String> mMap;
    
    public ConstraintHelper(final Context context) {
        super(context);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.mViews = null;
        this.mMap = new HashMap<Integer, String>();
        this.myContext = context;
        this.init(null);
    }
    
    public ConstraintHelper(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.mViews = null;
        this.mMap = new HashMap<Integer, String>();
        this.myContext = context;
        this.init(attrs);
    }
    
    public ConstraintHelper(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.mViews = null;
        this.mMap = new HashMap<Integer, String>();
        this.myContext = context;
        this.init(attrs);
    }
    
    protected void init(final AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
                    this.setIds(this.mReferenceIds = a.getString(attr));
                }
            }
        }
    }
    
    public void addView(final View view) {
        if (view.getId() == -1) {
            Log.e("ConstraintHelper", "Views added to a ConstraintHelper need to have an id");
            return;
        }
        if (view.getParent() == null) {
            Log.e("ConstraintHelper", "Views added to a ConstraintHelper need to have a parent");
            return;
        }
        this.mReferenceIds = null;
        this.addRscID(view.getId());
        this.requestLayout();
    }
    
    public void removeView(final View view) {
        final int id = view.getId();
        if (id == -1) {
            return;
        }
        this.mReferenceIds = null;
        for (int i = 0; i < this.mCount; ++i) {
            if (this.mIds[i] == id) {
                for (int j = i; j < this.mCount - 1; ++j) {
                    this.mIds[j] = this.mIds[j + 1];
                }
                this.mIds[this.mCount - 1] = 0;
                --this.mCount;
                break;
            }
        }
        this.requestLayout();
    }
    
    public int[] getReferencedIds() {
        return Arrays.copyOf(this.mIds, this.mCount);
    }
    
    public void setReferencedIds(final int[] ids) {
        this.mReferenceIds = null;
        this.mCount = 0;
        for (int i = 0; i < ids.length; ++i) {
            this.addRscID(ids[i]);
        }
    }
    
    private void addRscID(final int id) {
        if (this.mCount + 1 > this.mIds.length) {
            this.mIds = Arrays.copyOf(this.mIds, this.mIds.length * 2);
        }
        this.mIds[this.mCount] = id;
        ++this.mCount;
    }
    
    public void onDraw(final Canvas canvas) {
    }
    
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (this.mUseViewMeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        else {
            this.setMeasuredDimension(0, 0);
        }
    }
    
    public void validateParams() {
        if (this.mHelperWidget == null) {
            return;
        }
        final ViewGroup.LayoutParams params = this.getLayoutParams();
        if (params instanceof ConstraintLayout.LayoutParams) {
            final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)params;
            layoutParams.widget = (ConstraintWidget)this.mHelperWidget;
        }
    }
    
    private void addID(String idString) {
        if (idString == null || idString.length() == 0) {
            return;
        }
        if (this.myContext == null) {
            return;
        }
        idString = idString.trim();
        ConstraintLayout parent = null;
        if (this.getParent() instanceof ConstraintLayout) {
            parent = (ConstraintLayout)this.getParent();
        }
        int rscId = 0;
        if (parent != null) {
            rscId = this.findId(parent, idString);
        }
        if (rscId == 0 && this.isInEditMode()) {
            final Object value = parent.getDesignInformation(0, idString);
            if (value != null && value instanceof Integer) {
                rscId = (int)value;
            }
        }
        if (rscId == 0) {
            rscId = this.myContext.getResources().getIdentifier(idString, "id", this.myContext.getPackageName());
        }
        if (rscId != 0) {
            this.mMap.put(rscId, idString);
            this.addRscID(rscId);
        }
        else {
            Log.w("ConstraintHelper", "Could not find id of \"" + idString + "\"");
        }
    }
    
    private int findId(final ConstraintLayout container, final String idString) {
        if (idString == null || container == null) {
            return 0;
        }
        final Resources resources = this.getResources();
        if (resources == null) {
            return 0;
        }
        for (int count = container.getChildCount(), j = 0; j < count; ++j) {
            final View child = container.getChildAt(j);
            if (child.getId() != -1) {
                String res = null;
                try {
                    res = resources.getResourceEntryName(child.getId());
                }
                catch (Resources.NotFoundException ex) {}
                if (idString.equals(res)) {
                    return child.getId();
                }
            }
        }
        return 0;
    }
    
    protected void setIds(final String idList) {
        this.mReferenceIds = idList;
        if (idList == null) {
            return;
        }
        int begin = 0;
        this.mCount = 0;
        while (true) {
            final int end = idList.indexOf(44, begin);
            if (end == -1) {
                break;
            }
            this.addID(idList.substring(begin, end));
            begin = end + 1;
        }
        this.addID(idList.substring(begin));
    }
    
    public void updatePreLayout(final ConstraintLayout container) {
        if (this.isInEditMode()) {
            this.setIds(this.mReferenceIds);
        }
        if (this.mHelperWidget == null) {
            return;
        }
        if (this.mReferenceIds != null) {
            this.setIds(this.mReferenceIds);
        }
        this.mHelperWidget.removeAllIds();
        for (int i = 0; i < this.mCount; ++i) {
            final int id = this.mIds[i];
            View view = container.getViewById(id);
            if (view == null) {
                final String candidate = this.mMap.get(id);
                final int foundId = this.findId(container, candidate);
                if (foundId != 0) {
                    this.mIds[i] = foundId;
                    this.mMap.put(foundId, candidate);
                    view = container.getViewById(foundId);
                }
            }
            if (view != null) {
                this.mHelperWidget.add(container.getViewWidget(view));
            }
        }
        this.mHelperWidget.updateConstraints(container.mLayoutWidget);
    }
    
    public void updatePreLayout(final ConstraintWidgetContainer container, final Helper helper, final SparseArray<ConstraintWidget> map) {
        helper.removeAllIds();
        for (int i = 0; i < this.mCount; ++i) {
            final int id = this.mIds[i];
            helper.add((ConstraintWidget)map.get(id));
        }
    }
    
    protected View[] getViews(final ConstraintLayout layout) {
        if (this.mViews == null || this.mViews.length != this.mCount) {
            this.mViews = new View[this.mCount];
        }
        for (int i = 0; i < this.mCount; ++i) {
            final int id = this.mIds[i];
            this.mViews[i] = layout.getViewById(id);
        }
        return this.mViews;
    }
    
    public void updatePostLayout(final ConstraintLayout container) {
    }
    
    public void updatePostMeasure(final ConstraintLayout container) {
    }
    
    public void updatePostConstraints(final ConstraintLayout constainer) {
    }
    
    public void loadParameters(final ConstraintSet.Constraint constraint, final HelperWidget child, final ConstraintLayout.LayoutParams layoutParams, final SparseArray<ConstraintWidget> mapIdToWidget) {
        if (constraint.layout.mReferenceIds != null) {
            this.setReferencedIds(constraint.layout.mReferenceIds);
        }
        else if (constraint.layout.mReferenceIdString != null && constraint.layout.mReferenceIdString.length() > 0) {
            constraint.layout.mReferenceIds = this.convertReferenceString(this, constraint.layout.mReferenceIdString);
            child.removeAllIds();
            for (int i = 0; i < constraint.layout.mReferenceIds.length; ++i) {
                final int id = constraint.layout.mReferenceIds[i];
                final ConstraintWidget widget = (ConstraintWidget)mapIdToWidget.get(id);
                if (widget != null) {
                    child.add(widget);
                }
            }
        }
    }
    
    private int[] convertReferenceString(final View view, final String referenceIdString) {
        final String[] split = referenceIdString.split(",");
        final Context context = view.getContext();
        int[] rscIds = new int[split.length];
        int count = 0;
        for (int i = 0; i < split.length; ++i) {
            String idString = split[i];
            idString = idString.trim();
            int id = 0;
            try {
                final Class res = R.id.class;
                final Field field = res.getField(idString);
                id = field.getInt(null);
            }
            catch (Exception ex) {}
            if (id == 0) {
                id = context.getResources().getIdentifier(idString, "id", context.getPackageName());
            }
            if (id == 0 && view.isInEditMode() && view.getParent() instanceof ConstraintLayout) {
                final ConstraintLayout constraintLayout = (ConstraintLayout)view.getParent();
                final Object value = constraintLayout.getDesignInformation(0, idString);
                if (value != null && value instanceof Integer) {
                    id = (int)value;
                }
            }
            rscIds[count++] = id;
        }
        if (count != split.length) {
            rscIds = Arrays.copyOf(rscIds, count);
        }
        return rscIds;
    }
    
    public void resolveRtl(final ConstraintWidget widget, final boolean isRtl) {
    }
}
