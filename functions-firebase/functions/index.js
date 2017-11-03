const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// // Keeps track of the length of the 'likes' child list in a separate property.
exports.countlikechange_UFSM_Univ_velha = functions.database.ref('/linhas/Bairro-UFSM/Universidade - Faixa Velha').onWrite(event => {      
        const counterRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Universidade - Faixa Velha'); 
        const collectionRef = event.data.ref.root.child('/linhas/Bairro-UFSM/Universidade - Faixa Velha');
        
        // Return the promise from counterRef.set() so our function 
        // waits for this async event to complete before it exits.
        return collectionRef.once('value')
            .then(messagesData => counterRef.set(messagesData.numChildren()));
        }); 

exports.countlikechange_UFSM_Univ_nova = functions.database.ref('/linhas/Bairro-UFSM/Universidade - Faixa Nova').onWrite(event => {
        
        const counterRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Universidade - Faixa Nova'); 
        const collectionRef = event.data.ref.root.child('/linhas/Bairro-UFSM/Universidade - Faixa Nova');
        
        // Return the promise from counterRef.set() so our function 
        // waits for this async event to complete before it exits.
        return collectionRef.once('value')
            .then(messagesData => counterRef.set(messagesData.numChildren()));
        }); 
        
exports.countlikechange_UFSM_Bomb_nova = functions.database.ref('/linhas/Bairro-UFSM/Bombeiros - Faixa Nova').onWrite(event => {
    const counterRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Bombeiros - Faixa Nova'); 
    const collectionRef = event.data.ref.root.child('/linhas/Bairro-UFSM/Bombeiros - Faixa Nova');
    
    // Return the promise from counterRef.set() so our function 
    // waits for this async event to complete before it exits.
    return collectionRef.once('value')
        .then(messagesData => counterRef.set(messagesData.numChildren()));
    });

exports.countlikechange_UFSM_Bomb_velha = functions.database.ref('/linhas/Bairro-UFSM/Bombeiros - Faixa Velha').onWrite(event => {
    const counterRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Bombeiros - Faixa Velha'); 
    const collectionRef = event.data.ref.root.child('/linhas/Bairro-UFSM/Bombeiros - Faixa Velha');
    
    // Return the promise from counterRef.set() so our function 
    // waits for this async event to complete before it exits.
    return collectionRef.once('value')
        .then(messagesData => counterRef.set(messagesData.numChildren()));
    });
        
exports.countlikechange_Bairro_Univ_velha = functions.database.ref('/linhas/UFSM-Bairro/Universidade - Faixa Velha').onWrite(event => {
    const counterRef = event.data.ref.root.child('/solicit/UFSM-Bairro/Universidade - Faixa Velha'); 
    const collectionRef = event.data.ref.root.child('/linhas/UFSM-Bairro/Universidade - Faixa Velha');
    
    // Return the promise from counterRef.set() so our function 
    // waits for this async event to complete before it exits.
    return collectionRef.once('value')
        .then(messagesData => counterRef.set(messagesData.numChildren()));
    });

exports.countlikechange_Bairro_Univ_nova = functions.database.ref('/linhas/UFSM-Bairro/Universidade - Faixa Nova').onWrite(event => {
    const counterRef = event.data.ref.root.child('/solicit/UFSM-Bairro/Universidade - Faixa Nova'); 
    const collectionRef = event.data.ref.root.child('/linhas/UFSM-Bairro/Universidade - Faixa Nova');
    
    // Return the promise from counterRef.set() so our function 
    // waits for this async event to complete before it exits.
    return collectionRef.once('value')
        .then(messagesData => counterRef.set(messagesData.numChildren()));
    });

exports.countlikechange_Bairro_Bomb_nova = functions.database.ref('/linhas/UFSM-Bairro/Bombeiros - Faixa Nova').onWrite(event => {
    const counterRef = event.data.ref.root.child('/solicit/UFSM-Bairro/Bombeiros - Faixa Nova'); 
    const collectionRef = event.data.ref.root.child('/linhas/UFSM-Bairro/Bombeiros - Faixa Nova');
    
    // Return the promise from counterRef.set() so our function 
    // waits for this async event to complete before it exits.
    return collectionRef.once('value')
        .then(messagesData => counterRef.set(messagesData.numChildren()));
    });

exports.countlikechange_Bairro_Bomb_velha = functions.database.ref('/linhas/UFSM-Bairro/Bombeiros - Faixa Velha').onWrite(event => {
    const counterRef = event.data.ref.root.child('/solicit/UFSM-Bairro/Bombeiros - Faixa Velha'); 
    const collectionRef = event.data.ref.root.child('/linhas/UFSM-Bairro/Bombeiros - Faixa Velha');
    
    // Return the promise from counterRef.set() so our function 
    // waits for this async event to complete before it exits.
    return collectionRef.once('value')
        .then(messagesData => counterRef.set(messagesData.numChildren()));
    });
    

// If the number of likes gets deleted, recount the number of likes
// exports.recountlikes = functions.database.ref('/linhas/Bairro-UFSM/Universidade - Faixa Velha').onWrite(event => {
    
//     const counterRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Universidade - Faixa Velha'); 
//     const collectionRef = event.data.ref.root.child('/linhas/Bairro-UFSM/Universidade - Faixa Velha');
    
//     // Return the promise from counterRef.set() so our function 
//     // waits for this async event to complete before it exits.
//     return collectionRef.once('value')
//         .then(messagesData => counterRef.set(messagesData.numChildren()));
    
//   });

// exports.countlikechange_UFSM_Univ_nova = functions.database.ref('/linhas/Bairro-UFSM/Universidade - Faixa Nova').onWrite(event => {
//     const solicitacoesRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Universidade - Faixa Nova');
//     const collectionRef = event.data.ref.parent;
//     // const countRef = collectionRef.parent.child('likes_count');

//     return solicitacoesRef.set("0");
//     console.log('Counter updated. Counter value = ' + 0);

//   // Return the promise from countRef.transaction() so our function 
//   // waits for this async event to complete before it exits.
// //   return solicitacoesRef.transaction(current => {
// //     if (event.data.exists() && !event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) + 1);
// //     }
// //     else if (!event.data.exists() && event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) - 1);
// //     }
// //   }).then(() => {
// //     console.log('Counter updated. Counter value = ' + current);
// //   });
// });

// exports.countlikechange_UFSM_Bomb_nova = functions.database.ref('/linhas/Bairro-UFSM/Bombeiros - Faixa Nova').onWrite(event => {
//     const solicitacoesRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Bombeiros - Faixa Nova');
//     const collectionRef = event.data.ref.parent;
//     // const countRef = collectionRef.parent.child('likes_count');

//     return solicitacoesRef.set("0");
//     console.log('Counter updated. Counter value = ' + 0);

//   // Return the promise from countRef.transaction() so our function 
//   // waits for this async event to complete before it exits.
// //   return solicitacoesRef.transaction(current => {
// //     if (event.data.exists() && !event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) + 1);
// //     }
// //     else if (!event.data.exists() && event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) - 1);
// //     }
// //   }).then(() => {
// //     console.log('Counter updated. Counter value = ' + current);
// //   });
// });

// exports.countlikechange_UFSM_Bomb_velha = functions.database.ref('/linhas/Bairro-UFSM/Bombeiros - Faixa Velha').onWrite(event => {
//     const solicitacoesRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Bombeiros - Faixa Velha');
//     const collectionRef = event.data.ref.parent;
//     // const countRef = collectionRef.parent.child('likes_count');

//     return solicitacoesRef.set("0");
//     console.log('Counter updated. Counter value = ' + 0);

//   // Return the promise from countRef.transaction() so our function 
//   // waits for this async event to complete before it exits.
// //   return solicitacoesRef.transaction(current => {
// //     if (event.data.exists() && !event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) + 1);
// //     }
// //     else if (!event.data.exists() && event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) - 1);
// //     }
// //   }).then(() => {
// //     console.log('Counter updated. Counter value = ' + current);
// //   });
// });

// // Keeps track of the length of the 'likes' child list in a separate property.
// exports.countlikechange_Bairro_Univ_velha = functions.database.ref('/linhas/UFSM-Bairro/Universidade - Faixa Velha').onWrite(event => {
//     const solicitacoesRef = event.data.ref.root.child('/solicit/UFSM-Bairro/Universidade - Faixa Velha');
//     const collectionRef = event.data.ref.parent;
//     const countRef = collectionRef.parent.child('likes_count');

//     return solicitacoesRef.set("0");
//     console.log('Counter updated. Counter value = ' + 0);

//   // Return the promise from countRef.transaction() so our function 
//   // waits for this async event to complete before it exits.
// //   return solicitacoesRef.transaction(current => {
// //     if (event.data.exists() && !event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) + 1);
// //     }
// //     else if (!event.data.exists() && event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) - 1);
// //     }
// //   }).then(() => {
// //     console.log('Counter updated. Counter value = ' + current);
// //   });
// });

// exports.countlikechange_Bairro_Univ_nova = functions.database.ref('/linhas/UFSM-Bairro/Universidade - Faixa Nova').onWrite(event => {
//     const solicitacoesRef = event.data.ref.root.child('/solicit/UFSM-Bairro/Universidade - Faixa Nova');
//     const collectionRef = event.data.ref.parent;
//     // const countRef = collectionRef.parent.child('likes_count');

//     return solicitacoesRef.set("0");
//     console.log('Counter updated. Counter value = ' + 0);

//   // Return the promise from countRef.transaction() so our function 
//   // waits for this async event to complete before it exits.
// //   return solicitacoesRef.transaction(current => {
// //     if (event.data.exists() && !event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) + 1);
// //     }
// //     else if (!event.data.exists() && event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) - 1);
// //     }
// //   }).then(() => {
// //     console.log('Counter updated. Counter value = ' + current);
// //   });
// });

// exports.countlikechange_Bairro_Bomb_nova = functions.database.ref('/linhas/UFSM-Bairro/Bombeiros - Faixa Nova').onWrite(event => {
//     const solicitacoesRef = event.data.ref.root.child('/solicit/UFSM-Bairro/Bombeiros - Faixa Nova');
//     const collectionRef = event.data.ref.parent;
//     // const countRef = collectionRef.parent.child('likes_count');

//     return solicitacoesRef.set("0");
//     console.log('Counter updated. Counter value = ' + 0);

//   // Return the promise from countRef.transaction() so our function 
//   // waits for this async event to complete before it exits.
// //   return solicitacoesRef.transaction(current => {
// //     if (event.data.exists() && !event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) + 1);
// //     }
// //     else if (!event.data.exists() && event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) - 1);
// //     }
// //   }).then(() => {
// //     console.log('Counter updated. Counter value = ' + current);
// //   });
// });

// exports.countlikechange_Bairro_Bomb_velha = functions.database.ref('/linhas/UFSM-Bairro/Bombeiros - Faixa Velha').onWrite(event => {
//     const solicitacoesRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Bombeiros - Faixa Velha');
//     const collectionRef = event.data.ref.parent;
//     // const countRef = collectionRef.parent.child('likes_count');

//     return solicitacoesRef.set("0");
//     console.log('Counter updated. Counter value = ' + 0);

//   // Return the promise from countRef.transaction() so our function 
//   // waits for this async event to complete before it exits.
// //   return solicitacoesRef.transaction(current => {
// //     if (event.data.exists() && !event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) + 1);
// //     }
// //     else if (!event.data.exists() && event.data.previous.exists()) {
// //       return root.child('/solicit/{sentido}/{linha}/likes').set((current || 0) - 1);
// //     }
// //   }).then(() => {
// //     console.log('Counter updated. Counter value = ' + current);
// //   });
// });



//     const solicitacoesRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Universidade - Faixa Velha');
//     const collectionRef = event.data.ref.parent;
//     // const countRef = collectionRef.parent.child('likes_count');

//     // const number = event.data.val();
//     // console.log('Counter updated. Counter value = ' + number);
//     // solicitacoesRef.set(number + 1);
//     // return solicitacoesRef.set("0");
//     // console.log('Counter updated. Counter value = ' + 0);

//   // Return the promise from countRef.transaction() so our function 
//   // waits for this async event to complete before it exits.
//   return solicitacoesRef.transaction(current => {
//     if (event.data.exists() && !event.data.previous.exists()) {
//         console.log('Incrementa ' + 0);
//     }
//     else if (!event.data.exists() && event.data.previous.exists()) {
//         console.log('Decrementa ' + 0);
//     }
//   }).then(() => {
//     console.log('Counter updated. Counter value = ' + current);
//   });
    // const counterRef = event.data.ref;
    // const collectionRef = event.data.ref.root.child('/solicit/Bairro-UFSM/Universidade - Faixa Velha');
    
    // // Return the promise from counterRef.set() so our function 
    // // waits for this async event to complete before it exits.
    // return collectionRef.once('value')
    //     .then(messagesData => counterRef.set(messagesData.numChildren()));

//     const collectionRef = event.data.ref.parent;
//     const countRef = collectionRef.parent.child('likes_count');
  
//     // Return the promise from countRef.transaction() so our function 
//     // waits for this async event to complete before it exits.
//     return countRef.transaction(current => {
//       if (event.data.exists() && !event.data.previous.exists()) {
//         return (current || 0) + 1;
//       }
//       else if (!event.data.exists() && event.data.previous.exists()) {
//         return (current || 0) - 1;
//       }
//     }).then(() => {
//       console.log('Counter updated.');
//     });
  
// });

// // const functions = require('firebase-functions');

// // exports.count_bus_toUFSM_uni_fx_velha = functions.database
// //     .ref('')

// // // Create and Deploy Your First Cloud Functions
// // // https://firebase.google.com/docs/functions/write-firebase-functions

// // exports.helloWorld = functions.https.onRequest((request, response) => {
// //  response.send("Hello from Firebase!");
// // });