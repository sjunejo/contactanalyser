package in.sadrudd.contactanalyser;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.CallLog;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;

import java.util.HashMap;

import in.sadrudd.contactanalyser.data.CallLogDataAccessor;

/**
 * Created by sjunjo on 02/08/15.
 */
public class MockContactProviderTest extends AndroidTestCase {

    public void testMockPhoneNumbersFromContacts(){
        // Mock data goes here. TODO complete mock data test cases
        String[] mockData = {"(979) 267-8509", "FRED", ""};

        CallLogDataAccessor callLogDataAccessor = new CallLogDataAccessor();
        String[] columnsFromCallLogToReturn = callLogDataAccessor.getColumnsFromCallLogToReturn();


        MatrixCursor matrixCursor = new MatrixCursor(columnsFromCallLogToReturn);
        matrixCursor.addRow(mockData);

        //Step 2: Create a stub content provider and add the matrix cursor as the expected result of the query
        HashMapMockContentProvider mockProvider = new HashMapMockContentProvider();
        mockProvider.addQueryResult(CallLog.Calls.CONTENT_URI, matrixCursor);

        //Step 3: Create a mock resolver and add the content provider.
        MockContentResolver mockResolver = new MockContentResolver();
        mockResolver.addProvider(CallLog.AUTHORITY, mockProvider);

        //Step 4: Add the mock resolver to the mock context
        ContextWithMockContentResolver mockContext = new ContextWithMockContentResolver(super.getContext());
        mockContext.setContentResolver(mockResolver);

        //Example Test
        String result = callLogDataAccessor.getCallLogData(mockContext).toString();
        assertEquals("(979) 267-8509",result);
    }

    //Specialized Mock Content provider for step 2.  Uses a hashmap to return data dependent on the uri in the query
    public class HashMapMockContentProvider extends MockContentProvider {
        private HashMap<Uri, Cursor> expectedResults = new HashMap<Uri, Cursor>();
        public void addQueryResult(Uri uriIn, Cursor expectedResult){
            expectedResults.put(uriIn, expectedResult);
        }
        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
            return expectedResults.get(uri);
        }
    }

    public class ContextWithMockContentResolver extends RenamingDelegatingContext {
        private ContentResolver contentResolver;
        public void setContentResolver(ContentResolver contentResolver){ this.contentResolver = contentResolver;}
        public ContextWithMockContentResolver(Context targetContext) { super(targetContext, "test");}
        @Override public ContentResolver getContentResolver() { return contentResolver; }
        @Override public Context getApplicationContext(){ return this; }
    }


}
