package com.example.arthurpc.gps_application.services;

import com.example.arthurpc.gps_application.services.dto.AuthResponseDTO;

/**
 * Created by ArthurPC on 3/12/2016.
 */
public abstract class AuthResponseHandler {
    protected void OnSuccess(AuthResponseDTO authResult){}
    protected void OnError(Throwable e){}

}
