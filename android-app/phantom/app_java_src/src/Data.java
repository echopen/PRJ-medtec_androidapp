import java.io.*;

public class Data
{

    private String title;

    private String description;

    private int[] envelopeData;

    public Data(String title, String description, String file)
    {
        this.title = title;
        this.description = description;
        envelopeData = new int[0];
        setCharData(file);
    }


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {

        this.title = title;
    }

    public String getDesc()
    {

        return description;
    }


    public void setDesc(String description)
    {

        this.description = description;
    }


    public int[] getEnvelopeData()
    {
        return envelopeData;
    }


    public void setImage(int[] envelopeData)
    {
        this.envelopeData = envelopeData;
    }

    private void setCharData(String file_name){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String dir = new File(".").getAbsolutePath();

        try {
            br = new BufferedReader(new FileReader(dir + file_name));
            while ((line = br.readLine()) != null) {
                String[] string_tmp_data = line.split(cvsSplitBy);
                int[] char_tmp_data = new int[string_tmp_data.length];
                for (int index = 0; index < string_tmp_data.length; index++) {
                    char_tmp_data[index] = Integer.parseInt(string_tmp_data[index]);
                }
                envelopeData = Data.objArrayConcat(envelopeData, char_tmp_data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* this method can be useful if we have data to be concatenated in horizontal way */

    public static int[] objArrayConcat(int[] o1, int[] o2)
    {
        int[] ret = new int[o1.length + o2.length];

        System.arraycopy(o1, 0, ret, 0, o1.length);
        System.arraycopy(o2, 0, ret, o1.length, o2.length);

        return ret;
    }
}
