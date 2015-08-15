//package net.rimoto.android.HS;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.tenmiles.helpstack.logic.HSGear;
//import com.tenmiles.helpstack.logic.OnFetchedArraySuccessListener;
//import com.tenmiles.helpstack.logic.OnFetchedSuccessListener;
//import com.tenmiles.helpstack.logic.OnNewTicketFetchedSuccessListener;
//import com.tenmiles.helpstack.model.HSKBItem;
//import com.tenmiles.helpstack.model.HSTicket;
//import com.tenmiles.helpstack.model.HSUploadAttachment;
//import com.tenmiles.helpstack.model.HSUser;
//
//import net.rimoto.core.RimotoCore;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HSRimotoGear extends HSGear {
//    private static final String FAQ_URL = RimotoCore.getApiEndpoint()+"/faq";
//    private List<HSRimotoKBItem> mFaq = new ArrayList<>();
//
//    private void fetchFAQ(Response.Listener<List> success, Response.ErrorListener errorListener,
//                          RequestQueue queue) {
//        if(mFaq.size()>0) {
//            success.onResponse(mFaq);
//            return;
//        }
//
//        JsonArrayRequest request = new JsonArrayRequest(FAQ_URL, (json)-> {
//            for(int i=0;i<json.length();i++) {
//                try {
//                    JSONObject q = json.getJSONObject(i);
//
//                    HSKBItem article = HSKBItem.createForArticle(
//                            String.valueOf(i),
//                            q.getString("question"),
//                            q.getString("answer")
//                    );
//
//                    HSRimotoKBItem category = getCategory(q.getString("category"));
//                    category.getChildren().add(article);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            success.onResponse(mFaq);
//        }, errorListener);
//
//        // to avoid server overload call
//        request.setRetryPolicy(new DefaultRetryPolicy(2000, 1, 1f));
//        queue.add(request);
//        queue.start();
//    }
//
//    private HSRimotoKBItem getCategory(String categorty) {
//        HSRimotoKBItem category = new HSRimotoKBItem(categorty);
//
//        int contains = mFaq.indexOf(category);
//        if(contains==-1) {
//            mFaq.add(category);
//        } else {
//            category = mFaq.get(contains);
//        }
//
//        return category;
//    }
//
//    // If user taps on a section, then section is send as a paremeter to the function
//    @Override
//    public void fetchKBArticle(String cancelTag, HSKBItem section, RequestQueue queue,
//                               OnFetchedArraySuccessListener success, Response.ErrorListener errorListener) {
//        fetchFAQ((faq)->{
//
//            if(section == null) {
//                HSRimotoKBItem[] sections=new HSRimotoKBItem[faq.size()];
//                faq.toArray(sections);
//                success.onSuccess(sections);
//            } else {
//                List<HSKBItem> list = getCategory(section.getId()).getChildren();
//                HSKBItem[] items = new HSKBItem[list.size()];
//                list.toArray(items);
//                success.onSuccess(items);
//            }
//        }, errorListener, queue);
//    }
//
//    @Override
//    public void createNewTicket(String cancelTag, HSUser user, String subject, String body, HSUploadAttachment[] attachments, RequestQueue queue, OnNewTicketFetchedSuccessListener successListener, Response.ErrorListener errorListener) {
//        super.createNewTicket(cancelTag, user, subject, body, attachments, queue, successListener, errorListener);
//    }
//
//    @Override
//    public void fetchAllUpdateOnTicket(String cancelTag, HSTicket ticket, HSUser user, RequestQueue queue, OnFetchedArraySuccessListener success, Response.ErrorListener errorListener) {
//        super.fetchAllUpdateOnTicket(cancelTag, ticket, user, queue, success, errorListener);
//    }
//
//    @Override
//    public void addReplyOnATicket(String cancelTag, String message, HSUploadAttachment[] attachments, HSTicket ticket, HSUser user, RequestQueue queue, OnFetchedSuccessListener success, Response.ErrorListener errorListener) {
//        super.addReplyOnATicket(cancelTag, message, attachments, ticket, user, queue, success, errorListener);
//    }
//
//    @Override
//    public void uploadMessageAsHtmlString(boolean htmlEnabled) {
//        super.uploadMessageAsHtmlString(htmlEnabled);
//    }
//
//    @Override
//    public boolean canUplaodMessageAsHtmlString() {
//        return super.canUplaodMessageAsHtmlString();
//    }
//
//    @Override
//    public void setNumberOfAttachmentGearCanHandle(int numberOfAttachmentGearCanHandle) {
//        super.setNumberOfAttachmentGearCanHandle(numberOfAttachmentGearCanHandle);
//    }
//
//    @Override
//    public int getNumberOfAttachmentGearCanHandle() {
//        return super.getNumberOfAttachmentGearCanHandle();
//    }
//
//    @Override
//    public void ignoreTicketUpdateInformationAfterAddingReply(boolean canIgnore) {
//        super.ignoreTicketUpdateInformationAfterAddingReply(canIgnore);
//    }
//
//    @Override
//    public boolean canIgnoreTicketUpdateInformationAfterAddingReply() {
//        return super.canIgnoreTicketUpdateInformationAfterAddingReply();
//    }
//}
