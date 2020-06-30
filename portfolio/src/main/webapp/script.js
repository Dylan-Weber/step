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

async function loadComments() {
    const commentCountSelector = document.getElementById('comment-count-selector');
    const commentCount = commentCountSelector.value;
    const url = `/data?count=${commentCount}`;
    const data = await fetch(url);

    const comments = await data.json();
    let commentList = document.getElementById('comment-container');
    removeAllChildren(commentList);
    for (let comment of comments) {
        let commentDomObject = document.createElement('li');
        commentDomObject.innerText = comment;
        commentList.appendChild(commentDomObject);
    }
}

function removeAllChildren(node) {
    while (node.firstChild) {
        node.removeChild(node.lastChild);
    }
}

function deleteAllComments() {
    fetch('/delete-data', {method: 'POST'});
    loadComments();
}