package com.songdehuai.commonlib.utils.grantor;

import androidx.annotation.NonNull;

/**
 * 授权回调
 *
 * @author songdehuai
 */
public interface PermissionListener {

    /**
     * 通过授权
     *
     * @param permission
     */
    void permissionGranted(@NonNull String[] permission);

    /**
     * 拒绝授权
     *
     * @param permission
     */
    void permissionDenied(@NonNull String[] permission);
}
