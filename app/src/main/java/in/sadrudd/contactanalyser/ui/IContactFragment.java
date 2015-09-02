package in.sadrudd.contactanalyser.ui;

/**
 * Created by sjunjo on 30/08/15.
 */
public interface IContactFragment {

    public void setData(String[] data);

    public String[] getData();

    public CheckBoxListAdapter getAdapter();

}
