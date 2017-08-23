package com.example.daniel_szabo.sensors;

import android.app.Service;
import android.os.Binder;

public class ServiceBinder<S extends Service> extends Binder {

    private final S service;

    public ServiceBinder(S service) {
        this.service = service;
    }

    public S getService() {
        return service;
    }
}
