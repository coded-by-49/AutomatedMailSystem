Java SMTP Email Automator
1. The "Why": A Note on Engineering

I didn't start this project to build something flashy. I started it to reclaim the learning process.

In an era of auto-generating code, I wanted to understand what it actually means to be a Software Engineer. My goals were specific:

    To master documentation: Reading and implementing the Jakarta Mail specifications directly, rather than guessing.

    To refine inquiry: Learning to ask the right questions at the right time to diagnose root causes.

    To collaborate with AI, not submit to it: Using AI tools to sharpen my reasoning and challenge my assumptions, without outsourcing the actual thinking.

This repository is the outcome of that journey: a robust, secure email automation tool built on a deep understanding of network protocols.
2. Technical Deep Dive: "What the Hell Did I Do?"

On the surface, this is a Java application that sends emails via Gmail. Under the hood, it is an implementation of Authenticated SMTP over Implicit TLS, built to bypass network-level restrictions.
The Architecture

    Language: Java

    API: Jakarta Mail (Eclipse Angus implementation)

    Protocol: SMTP (Simple Mail Transfer Protocol)

    Security: SSL/TLS 1.3

The Critical Engineering Challenge: The "Middlebox" Block

During development, the standard implementation using Port 587 (STARTTLS) failed consistently with EOF (End of File) and Connection Reset errors.

The Diagnosis: Through network testing (curl/telnet verification), I discovered that the EOF was not a server rejection, but a Middlebox interference. The local network firewall/ISP was performing Deep Packet Inspection (DPI) on Port 587. It saw the initial plain-text handshake of the STARTTLS protocol and terminated the connection before encryption could begin.

The Solution: Implicit TLS (Port 465) I re-engineered the transport layer to use Port 465.

    Why this worked: Port 465 uses "Implicit TLS." The connection is encrypted immediately upon the TCP handshake.

    The Result: To the firewall, the traffic appears as a stream of random, encrypted bytes. It cannot inspect the "Hello" packets, so it allows the connection to pass through to smtp.gmail.com.