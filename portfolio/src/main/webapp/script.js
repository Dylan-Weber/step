// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function loadPage() {
  loadUserDataReliantElements();
  loadComments();
}

async function loadUserDataReliantElements() {
  const userData = await getUserData();
  loadAuthenticationReliantElements(userData);
  loadCommentForm(userData);
  loadNicknameForm(userData);
}

async function getUserData() {
  const serverData = await fetch('/user-data');
  const userData = await serverData.json();
  return userData;
}

async function loadAuthenticationReliantElements(userData) {
  const loggedInElements = document.getElementsByClassName('logged-in');
  const loggedOutElements = document.getElementsByClassName('logged-out');
  
  for (let element of loggedInElements) {
    element.style.display = (userData.loggedIn) ? 'block' : 'none';
  }

  for (let element of loggedOutElements) {
    element.style.display = (userData.loggedIn) ? 'none' : 'block';
  }
}


async function submitForm(form, url) {
  let params = new URLSearchParams(new FormData(form));
  
  return fetch(url, { 
    method: 'POST', 
    body: params, 
    headers: { 'Content-type': 'application/x-www-form-urlencoded' }
  });
}

async function loadNicknameForm(userData) {
  attachNicknameFormSubmissionEvent();
  loadNicknameFormUsername(userData);
  clearNicknameInput();
}


function loadNicknameFormUsername(userData) {
  let nicknameFormUsername = document.getElementById("nickname-form-username");
  nicknameFormUsername.innerText = userData.nickname;
}

function clearNicknameInput() {
  const nicknameInput = document.getElementById('nickname-input');
  nicknameInput.value = '';
}

function attachNicknameFormSubmissionEvent() {
  const nicknameForm = document.getElementById('nickname-form');
  nicknameForm.addEventListener('submit', event => {
    event.preventDefault();
    submitNickname(event.target);
  });
}

async function submitNickname(form) {
  await submitForm(form, '/user-data');

  const userData = await getUserData();

  loadNicknameFormUsername(userData);
  loadComments(userData); // Reload the comments because the user's nickname may have changed in them
}

function loadCommentForm(userData) {
  attachCommentFormSubmissionEvent();
}

function attachCommentFormSubmissionEvent() {
  const commentForm = document.getElementById('comment-form');
  commentForm.addEventListener('submit', event => {
    event.preventDefault();
    submitComment(event.target);
  });
}

async function submitComment(form) {
  await submitForm(form, '/data');

  loadComments();
  const commentInputArea = document.getElementById('comment-input-area');
  commentInputArea.value = '';
}

async function loadComments() {
  const serverResponse = await requestCommentData();
  setPageSelector(serverResponse.numberOfPages);
  replaceComments(serverResponse.comments);
}

async function requestCommentData() {
  const commentCountSelector = document.getElementById('comment-count-selector');
  const commentPageSelector = document.getElementById('comment-page-selector');
  const commentCount = commentCountSelector.value;
  const commentPage = commentPageSelector.value;

  let params =  new URLSearchParams();
  params.append("count", commentCount);
  params.append("page", commentPage);

  const url = `/data?` + params.toString();
  const data = await fetch(url);
  const serverResponse = await data.json();
  return serverResponse;
}

function setPageSelector(numberOfPages) {
  const commentPageSelector = document.getElementById('comment-page-selector');
  commentPageSelector.max = numberOfPages;

  const currentSelection = commentPageSelector.value;
  commentPageSelector.value = Math.min(numberOfPages, currentSelection);
}

function replaceComments(newComments) {
  let commentContainer = document.getElementById('comment-container');
  removeAllChildren(commentContainer);
  if (newComments) {
    for (let comment of newComments) {
      let author =  comment.author;
      if (author === undefined) {
        author = "Unknown"
      }
      const displayText = `${author}: ${comment.content}`;
      let commentDomObject = document.createElement('li');
      commentDomObject.innerText = displayText;
      commentContainer.appendChild(commentDomObject);
    }
  }
}

function removeAllChildren(node) {
  while (node.firstChild) {
      node.removeChild(node.lastChild);
  }
}

async function deleteAllComments() {
  await fetch('/delete-data', {method: 'POST'});
  loadComments();
}