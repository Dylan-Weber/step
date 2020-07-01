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

async function submitComment() {
    const commentInputArea = document.getElementById('comment-input-area');
    const commentText = commentInputArea.value;
    await fetch(`/data?comment=${commentText}`, {method: 'POST'});
    loadComments();
    commentInputArea.value = '';
}

async function loadComments() {
    const commentCountSelector = document.getElementById('comment-count-selector');
    const commentPageSelector = document.getElementById('comment-page-selector');
    const commentCount = commentCountSelector.value;
    const commentPage = commentPageSelector.value;
    const url = `/data?count=${commentCount}&page=${commentPage}`;
    const data = await fetch(url);

    const serverResponse = await data.json();
    let commentContainer = document.getElementById('comment-container');
    removeAllChildren(commentContainer);
    for (let comment of serverResponse) {
        let commentDomObject = document.createElement('li');
        commentDomObject.innerText = comment;
        commentContainer.appendChild(commentDomObject);
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