package com.simonag.simonag;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by diditsepiyanto on 11/1/17.
 */

public class MultipartRequest extends Request<String> {
    public static final String KEY_PICTURE = "mypicture";
    public static final String KEY_PICTURE_NAME = "filename";
    public static final String KEY_ROUTE_ID = "route_id";

    private HttpEntity mHttpEntity;

    private String id_aktivitas;
    private String tanggalRealisasi;
    private String keterangan;
    private String realisasiNilai;
    private String revenueRealisasiNilai;

    private Response.Listener mListener;

    public MultipartRequest(String url, String filePath, String tanggalRealisasi, String keterangan, String realisasiNilai, String revenueRealisasiNilai,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mHttpEntity = buildMultipartEntity(filePath);
    }

    public MultipartRequest(String url, String filePath, String id_aktivitas, String tanggalRealisasi, String keterangan, String realisasiNilai, String revenueRealisasiNilai,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.id_aktivitas = id_aktivitas;
        this.tanggalRealisasi = tanggalRealisasi;
        this.keterangan = keterangan;
        this.realisasiNilai = revenueRealisasiNilai;
        this.revenueRealisasiNilai = revenueRealisasiNilai;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(filePath);
    }

    private HttpEntity buildMultipartEntity(String filePath) {
        File file = new File(filePath);
        return buildMultipartEntity(file);
    }

    private HttpEntity buildMultipartEntity(File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        FileBody fileBody = new FileBody(file);
        builder.addPart("attachment", fileBody);
        builder.addTextBody("id_aktivitas", id_aktivitas);
        builder.addTextBody("tanggal_realisasi", tanggalRealisasi);
        builder.addTextBody("keterangan", keterangan);
        builder.addTextBody("realisasi_nilai", realisasiNilai);
        builder.addTextBody("revenue_realisasi_nilai", revenueRealisasiNilai);
        return builder.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success("Uploaded", getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
