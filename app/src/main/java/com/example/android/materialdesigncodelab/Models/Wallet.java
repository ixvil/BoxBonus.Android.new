package com.example.android.materialdesigncodelab.Models;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by shipin_a on 24.10.2016.
 */

public class Wallet {

    private static Integer walletId = 0000000000;

    static public Integer getWalletId() {
        return walletId;
    }

    /**
     * Generate QR Bitmap with walletId
     *
     * @return Bitmap
     */
    static public Bitmap getQr(int pic_width, int pic_height) {
        Bitmap bitmap = null;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.encodeBitmap(walletId.toString(), BarcodeFormat.QR_CODE, pic_width, pic_height);

        } catch (WriterException e) {

        }
        return bitmap;
    }

    public static void setWalletId(int walletId) {
        Wallet.walletId = walletId;
    }
}