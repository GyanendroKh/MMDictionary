package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class VersionUpdateFragment extends DialogFragment {

  public static VersionUpdateFragment createFragment() {
    return new VersionUpdateFragment();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Update");
    builder.setMessage("There is a new version available");

    builder.setPositiveButton("Update", (dialog, id) -> openDownloadPage());

    builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

    return builder.create();
  }

  public void openDownloadPage() {
    String url = "https://meiteimayek.gyanendrokh.me/dictionary#download";
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }

}
