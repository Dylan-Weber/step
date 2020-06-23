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

/**
 * Adds a random cartoon quote to the page.
 */
function addRandomCartoonQuote() {
    const quotes = [
        {quoteText: 'If every porkchop were perfect, we wouldn\'t have hot dogs', author: 'Greg Universe'},
        {quoteText: 'Bacon pancakes, makin\' bacon pancakes.\n' +
        'Take some bacon and I\'ll put it in a pancake.\n' +
        'Bacon pancakes that\'s what it\'s gonna make,\n' +
        'Bacon pancaaake!',
        author: 'Jake the Dog'},
        {quoteText: 'Now the pig goes wherever he can shine the light of knowledge into the darkness of ignorance.', author: 'Waddles'}
    ]
  
    const quote = quotes[Math.floor(Math.random() * quotes.length)];

    const formattedQuoteText = `${quote.quoteText}\n- ${quote.author}`

    const quoteContainer = document.getElementById('quote-container');
    quoteContainer.innerText = formattedQuoteText;
}