//package net.rimoto.android.HS;
//
//import com.tenmiles.helpstack.model.HSKBItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class HSRimotoKBItem extends HSKBItem {
//    private List<HSKBItem> children = new ArrayList<>();
//
//    public HSRimotoKBItem(String category) {
//        super(category,category,null);
//        setArticleType(TYPE_SECTION);
//    }
//
//    public List<HSKBItem> getChildren() {
//        return children;
//    }
//
//    public void setChildren(List<HSKBItem> children) {
//        this.children = children;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        return (o instanceof HSRimotoKBItem) && (((HSRimotoKBItem) o).getId().equals(this.getId()));
//    }
//}
