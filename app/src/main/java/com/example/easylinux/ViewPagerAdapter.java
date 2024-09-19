package com.example.easylinux;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new allcomands();  // First tab - All Commands
            case 1:
                return new favorites();    // Second tab - Favorites
            default:
                return new allcomands();  // Default to All Commands
        }
    }

    @Override
    public int getItemCount() {
        return 2;  // We have 2 tabs
    }
}
