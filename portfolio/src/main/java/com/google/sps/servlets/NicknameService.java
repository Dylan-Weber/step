package com.google.sps.servlets;

interface NicknameService {
  void setNickname(String id, String email, String nickname);
  String getNicknameFromEmail(String email);
}